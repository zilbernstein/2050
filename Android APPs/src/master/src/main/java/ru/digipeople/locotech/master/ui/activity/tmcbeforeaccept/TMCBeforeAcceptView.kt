package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept

import ru.digipeople.locotech.master.model.WriteOffTmc
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
interface TMCBeforeAcceptView: MvpView {
    /**
     * Установка данных
     */
    fun setData(list: List<WriteOffTmc>)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Диалог списания ТМЦ
     */
    fun showWriteOffDialog(shouldShowOverrunWarning: Boolean)
}