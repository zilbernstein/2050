package ru.digipeople.locotech.inspector.ui.activity.comment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.comment.interactor.CommentUploader
import ru.digipeople.locotech.inspector.ui.activity.inspection.StartTab
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
                                           private val navigator: Navigator,
                                           private val commentUploader: CommentUploader) : BaseMvpViewStatePresenter<CommentView, CommentViewState>(viewState) {
    //region Other
    /**
     * Подключение логгирования
     */
    val logger = LoggerFactory.getLogger(CommentPresenter::class)
    private var commentDisposable = Disposables.disposed()
    private var startTab = StartTab.CYCLIC_WORK
    //endRegion
    override fun onInitialize() {
        view.setText(commentParams.text ?: "")
    }
    /**
     * переход назад
     */
    fun backButtonClicked() {
        view.navigateBack()
    }
    /**
     * Обработка нажатия кнопки принять
     */
    fun checkButtonClicked(text: String) {
        when (callingId) {
            CommentActivity.WORK_CALLING_ID, CommentActivity.RETURN_CALLING_ID -> addWorkComment(text)
            CommentActivity.REMARK_CALLING_ID -> addRemarkComment(text)
            CommentActivity.CSO_CALLING_ID -> addCsoComment(text)
        }
    }
    /**
     * Добавить комментарий к работе
     */
    private fun addWorkComment(text: String) {
        commentDisposable = commentUploader.addWorkComment(commentParams.id, text)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обрабокта резльтата
                     */
                    if (result.isSuccessful) {
                        view.finishActivity()
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * Добавить комментарий к замечанию
     */
    private fun addRemarkComment(text: String) {
        commentDisposable = commentUploader.addRemarkComment(commentParams.id, text)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        /**
                         * Переход к стартовой вкладке
                         */
                        when (startTab) {
                            StartTab.CYCLIC_WORK -> {
                                navigator.navigateToInspectionActivity()
                            }
                            StartTab.REMARK_OTK -> {
                                navigator.navigateToInspectionActivityWithOtkRemark()
                            }
                            StartTab.REMARK_RZD -> {
                                navigator.navigateToInspectionActivityWithRzdRemark()
                            }
                        }
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * добавить комментарий к ксо
     */
    private fun addCsoComment(text: String) {
        commentDisposable = commentUploader.addCsoComment(commentParams.id, text)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        view.finishActivity()
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * Изменение комментария
     */
    fun onCommentChange(newComment: String) {
        view.setText(newComment)
    }

    fun setStartTab(startTab: StartTab) {
        this.startTab = startTab
    }

}