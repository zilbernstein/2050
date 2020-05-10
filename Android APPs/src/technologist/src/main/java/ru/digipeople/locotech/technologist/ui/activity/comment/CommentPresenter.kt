package ru.digipeople.locotech.technologist.ui.activity.comment

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.locotech.technologist.ui.activity.comment.interactor.CommentWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер для структуры коментария
 *
 * @author Sostavkin Grisha
 */
class CommentPresenter @Inject constructor(viewState: CommentViewState,
                                           private val commentParams: CommentParams,
                                           private val commentWorker: CommentWorker,
                                           private val navigator: Navigator)
    : BaseMvpViewStatePresenter<CommentView, CommentViewState>(viewState) {

    private var disposable: Disposable = Disposables.disposed()
    /**
     * Действия при инициализации презентера
     */
    override fun onInitialize() {}

    fun onScreenShown() {
        view.setText(commentParams.text)
    }
    /**
     * обработка нажатия назад
     */
    fun backButtonClicked() {
        navigator.navigateBack()
    }
    /**
     * редактировать комментарий
     */
    fun checkButtonClicked(text: String) {
        disposable.dispose()
        disposable = commentWorker.editComment(commentParams.id, text)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoading(false)
                    if (result.isSuccessful) {
                        navigator.navigateBack()
                    }else{
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoading(false)
                    view.showError(it.message!!)
                })
    }
}