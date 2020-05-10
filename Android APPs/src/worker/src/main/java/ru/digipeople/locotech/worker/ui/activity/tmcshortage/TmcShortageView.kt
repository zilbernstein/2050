package ru.digipeople.locotech.worker.ui.activity.tmcshortage

import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.locotech.worker.model.TMCInWork

/**
 * Интерфейс структуры ТМЦ
 *
 * @author Kashonkov Nikita
 */
interface TmcShortageView : MvpView {
    /**
     * Обновление данных в адаптере
     */
    fun updateAdapter()
    /**
     * Установка данных в адаптер
     */
    fun setDataToAdapter(allList: List<TMCInWork>, currentList: MutableList<TMCInWork>)
    /**
     * Запрос причины остановки
     */
    fun showCheckMistake()
    /**
     * Завершение активити с выдачей результата
     */
    fun finishActivityWithResult()
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}