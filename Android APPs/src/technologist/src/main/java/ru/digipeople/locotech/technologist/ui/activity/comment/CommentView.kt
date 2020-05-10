package ru.digipeople.locotech.technologist.ui.activity.comment

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс комментариев
 *
 * @author Sostavkin Grisha
 */
interface CommentView: MvpView {
    /**
     * Установка текста комментария
     */
    fun setText(text: String)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Управление видимостью лоадера
     */
    fun setLoading(isLoading: Boolean)
}