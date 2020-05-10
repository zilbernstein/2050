package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
interface WriteOffTTmcAmountView: MvpView{
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
}