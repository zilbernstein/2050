package ru.digipeople.locotech.master.ui.activity.urgent

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс срочно
 *
 * @author Kashonkov Nikita
 */
interface UrgentView : MvpView {
    /**
     * Установка данных в адаптер
     */
    fun setDataToAdapter(list: List<Work>)
    /**
     * Управление видимостью ладера
     */
    fun setLoadingVisible(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}