package ru.digipeople.locotech.inspector.ui.activity.comment

import ru.digipeople.ui.mvp.view.MvpView

/**
 * интерфейс комментариев
 *
 * @author Kashonkov Nikita
 */
interface CommentView : MvpView {
    /**
     * Переход назад
     */
    fun navigateBack()
    /**
     * Действия при завершении активити
     */
    fun finishActivity()
    /**
     * Установить текст комментария
     */
    fun setText(text: String)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}