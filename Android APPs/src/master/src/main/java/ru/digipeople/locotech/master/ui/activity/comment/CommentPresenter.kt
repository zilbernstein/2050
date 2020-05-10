package ru.digipeople.locotech.master.ui.activity.comment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.master.ui.activity.comment.interactor.CommentUploader
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер комментариев
 *
 * @author Kashonkov Nikita
 */
class CommentPresenter @Inject constructor(viewState: CommentViewState,
                                           private val callingId: Int,
                                           private val commentParams: CommentParams,
                                           private val commentUploader: CommentUploader)
    : BaseMvpViewStatePresenter<CommentView, CommentViewState>(viewState) {
    //region Other
    val logger = LoggerFactory.getLogger(CommentPresenter::class)
    private var commentDisposable = Disposables.disposed()
    //endRegion
    /**
     * операции при инициализации презентера
     */
    override fun onInitialize() {}

    /**
     * обработка нажатия назад
     */
    fun backButtonClicked() {
        view.navigateBack()
    }
    /**
     * обработка кнопки принять
     */
    fun checkButtonClicked(text: String) {
        when (callingId) {
            CommentActivity.WORK_CALLING_ID, CommentActivity.RETURN_CALLING_ID -> addWorkComment(text)
            CommentActivity.REMARK_CALLING_ID -> addRemarkComment(text)
        }
    }
    /**
     * операции при отрисовке экрана
     */
    fun onScreenShown() {
        view.setText(commentParams.text)
    }
    /**
     * добавление комментария к работе
     */
    private fun addWorkComment(text: String) {
        commentDisposable = commentUploader.addWorkComment(commentParams.id, text)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        view.finishActivity()
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * добавление комментария к замечанию
     */
    private fun addRemarkComment(text: String) {
        commentDisposable = commentUploader.addRemarkComment(commentParams.id, text)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        /**
                         * обработка результата
                         */
                        view.finishActivity()
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
}