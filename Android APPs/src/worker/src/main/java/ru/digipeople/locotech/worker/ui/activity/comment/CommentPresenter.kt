package ru.digipeople.locotech.worker.ui.activity.comment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.worker.ui.activity.comment.interactor.CommentUploader
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер для структуры коментария
 *
 * @author Kashonkov Nikita
 */
class CommentPresenter @Inject constructor(viewState: CommentViewState,
                                           private var commentParams: CommentParams,
                                           private var commentUploader: CommentUploader) :
        BaseMvpViewStatePresenter<CommentView, CommentViewState>(viewState) {
    // region Other
    private var commentDisposable = Disposables.disposed()
    private var disposable = Disposables.disposed()
    //endRegion
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}
    /**
     * Нажатие назад
     */
    fun backButtonClicked() {
        view.navigateBack()
    }
    /**
     * Сохранить комментарий
     */
    fun checkButtonClicked(text: String) {
        commentDisposable = commentUploader.addWorkComment(commentParams.workId, text)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработа результата
                     */
                    if (result.isSuccessful) {
                        view.navigateToPhoto(commentParams.workId)
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

    fun photoResultReturned() {
        view.finishActivityWithResult()
    }

    fun onScreenShown() {
        view.setText(commentParams.text)
    }
}