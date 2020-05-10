package ru.digipeople.locotech.master.ui.activity.partlyaccept

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс частичной приемки
 *
 * @author Kashonkov Nikita
 */
interface PartlyAcceptView: MvpView {
    /**
     * управление видимостью ладера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение данных по частичной приемке
     */
    fun showModel(model: Work?)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * установка процентов
     */
    fun setPercent(percent: String)
    /**
     * установка новых процентов
     */
    fun setNewOutfitPercent(percent: Int)
    /**
     * установка нового времени
     */
    fun setNewOutfitTime(time: Long)
    /**
     * усправление видимостью информации частичной приемки
     */
    fun setNewOutfitInfoVisibility(isVisible: Boolean)
    /**
     * управление доступностью кнопки
     */
    fun setAcceptButtonEnability(isEnable: Boolean)
}