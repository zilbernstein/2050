package ru.digipeople.locotech.worker.ui.activity.shift

import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.locotech.worker.model.Client

/**
 * Интерфейс структуры смены
 *
 * @author Kashonkov Nikita
 */
interface ShiftView : MvpView {

    /**
     * установка данных
     */
    fun setUpView(client: Client, isInWork: Boolean)
    /**
     * Отображение статуса и наименования кнопки
     */
    fun showWorkStatus(isInWork: Boolean)
    /**
     * Управлние видимостью лоадера
     */
    fun setLoading(isLoading: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}