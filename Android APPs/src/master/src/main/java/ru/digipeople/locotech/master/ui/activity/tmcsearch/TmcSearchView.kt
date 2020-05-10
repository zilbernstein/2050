package ru.digipeople.locotech.master.ui.activity.tmcsearch

import ru.digipeople.locotech.master.model.TMC
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
interface TmcSearchView : MvpView {
    /**
     * Установка данных
     */
    fun setItems(items: List<TMC>)
    /**
     * Управление видимости лоадера
     */
    fun setLoadingVisible(visible: Boolean)
    /**
     * Отображение сообщение об отсутствии данных
     */
    fun setNoDataMsgVisible(visible: Boolean)
    /**
     * Установка видимости хедера
     */
    fun setHeaderVisible(visible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(error: UserError)
}