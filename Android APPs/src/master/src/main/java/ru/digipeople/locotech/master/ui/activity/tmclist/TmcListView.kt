package ru.digipeople.locotech.master.ui.activity.tmclist

import ru.digipeople.locotech.master.model.TMCInWork
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
interface TmcListView : MvpView {
    /**
     * Установка данных
     */
    fun setData(list: List<TMCInWork>)
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Отображение диалога удаления
     */
    fun showDeleteDialog()
}