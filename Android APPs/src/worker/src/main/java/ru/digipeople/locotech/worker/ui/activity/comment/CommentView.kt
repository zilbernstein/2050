package ru.digipeople.locotech.worker.ui.activity.comment

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс экрана комментарий
 *
 * @author Kashonkov Nikita
 */
interface CommentView : MvpView {
    /**
     * Переход на предыдущий экран
     */
    fun navigateBack()
    /**
     * Переход к фото
     */
    fun navigateToPhoto(taskId: String)
    /**
     * Завершение активити с передачей результата
     */
    fun finishActivityWithResult()
    /**
     * Установка текста в поле
     */
    fun setText(text: String)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Управление видимостью лодера
     */
    fun setLoading(isLoading: Boolean)
}