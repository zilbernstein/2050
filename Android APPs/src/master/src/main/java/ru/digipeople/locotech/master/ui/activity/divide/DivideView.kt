package ru.digipeople.locotech.master.ui.activity.divide

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс разделения работы
 *
 * @author Kashonkov Nikita
 */
interface DivideView: MvpView {
    /**
     * отображение данных по разделению работы
     */
    fun showModel(work: Work?)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * установка времени
     */
    fun setTime(time: Long)
    /**
     * установка процентов
     */
    fun setPercent(percent: String)
    /**
     * установка процентов после изменения времени
     */
    fun setPercentAfterTimeChanged(percent: String)
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * управление доступностью кнопки разделения
     */
    fun setDivideButtonEnabled(isEnable: Boolean)
    /**
     * показ диалога выбора времени
     */
    fun showTimePickerDialog(initTIme: Long, maxTime: Long)
    /**
     * очистка времени
     */
    fun hideDividedTime()
    /**
     * очистка процентов
     */
    fun hidePercent()
}