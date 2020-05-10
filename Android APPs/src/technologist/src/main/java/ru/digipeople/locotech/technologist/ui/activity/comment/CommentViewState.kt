package ru.digipeople.locotech.technologist.ui.activity.comment

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState комментария
 *
 * @author Sostavkin Grisha
 */
class CommentViewState @Inject constructor() : BaseMvpViewState<CommentView>(), CommentView {

    override fun onViewAttached(view: CommentView?) {}

    override fun onViewDetached(view: CommentView?) {}
    /**
     * Установка текста комментария
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
     * Управление видимостью лоадера
     */
    override fun setLoading(isLoading: Boolean) {
        forEachView { it.setLoading(isLoading) }
    }
}