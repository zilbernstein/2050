package ru.digipeople.locotech.master.ui.activity.searchperformer

import ru.digipeople.locotech.master.model.PerformerItem
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
interface SearchPerformerView : MvpView {
    /**
     * Установка исполниетелей
     */
    fun setWorkers(list: List<PerformerItem>)
    /**
     * Установка исполниетелей в работу
     */
    fun setWorkersInWork(list: List<PerformerItem>)
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Управление видимостью лоадера
     */
    fun showError(message: String)
    /**
     * Отображение ошибки перегрузки сиполниетел
     */
    fun showOverLoadError()
    /**
     * Отображение ошибки отсутсвия проставленной явки
     */
    fun showNullShiftError()
    /**
     * Отображение данных
     */
    fun showWorkModel(work: Work)
}