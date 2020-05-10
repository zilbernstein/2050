package ru.digipeople.locotech.master.ui.activity.tmcamount

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
interface TmcAmountView: MvpView {
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
}