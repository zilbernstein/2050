package ru.digipeople.locotech.worker.ui.activity.comment

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState комментария
 *
 * @author Kashonkov Nikita
 */
class CommentViewState @Inject constructor() : BaseMvpViewState<CommentView>(), CommentView {
    var returnResultFlag = false
    override fun onViewAttached(view: CommentView) {
        if(returnResultFlag){
            view.finishActivityWithResult()
        }
    }

    override fun onViewDetached(view: CommentView) {}

    override fun finishActivityWithResult() {
        returnResultFlag = true
        forEachView { it.finishActivityWithResult() }
    }
    /**
     * Переход на предыдущий экран
     */
    override fun navigateBack() {
        forEachView { it.navigateBack() }
    }
    /**
     * Установка текста в поле
     */
    override fun setText(text: String) {
        forEachView { it.setText(text) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Переход к фото
     */
    override fun navigateToPhoto(taskId: String) {
        forEachView { it.navigateToPhoto(taskId) }
    }
    /**
     * Управление видимостьюю лодера
     */
    override fun setLoading(isLoading: Boolean){
        forEachView { it.setLoading(isLoading) }
    }
}