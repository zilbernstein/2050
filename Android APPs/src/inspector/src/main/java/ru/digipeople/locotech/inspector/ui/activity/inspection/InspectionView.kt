package ru.digipeople.locotech.inspector.ui.activity.inspection

import ru.digipeople.ui.mvp.view.MvpView
/**
 * Интерфейс инспекционного контроля
 *
 * @author Kashonkov Nikita
 */
interface InspectionView : MvpView {
    /**
     * Установка заголовка
     */
    fun setTitle(equipmentTitle: String?)
    /**
     * Управление видимостью лоадера
     */
    fun setProgressVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Сообщение пользвателю
     */
    fun showSuccessfullyNotifiedMessage()
}