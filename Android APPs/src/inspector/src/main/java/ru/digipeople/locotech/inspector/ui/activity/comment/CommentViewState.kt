package ru.digipeople.locotech.inspector.ui.activity.comment

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * Вью стейт комментариев
 *
 * @author Kashonkov Nikita
 */
class CommentViewState @Inject constructor() : BaseMvpViewState<CommentView>(), CommentView {

    private var text: String? = null

    override fun onViewAttached(view: CommentView) {
        text?.let {
            view.setText(it)
        }
    }

    override fun onViewDetached(view: CommentView?) {}
    /**
     * Действия при завершении активити
     */
    override fun finishActivity() {
        forEachView { it.finishActivity() }
    }
    /**
     * Переход назад
     */
    override fun navigateBack() {
        forEachView { it.navigateBack() }
    }
    /**
     * Установить текст комментария
     */
    override fun setText(text: String) {
        this.text = text
        forEachView { it.setText(text) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}