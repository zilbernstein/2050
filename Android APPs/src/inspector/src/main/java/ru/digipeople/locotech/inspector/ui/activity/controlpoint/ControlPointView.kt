package ru.digipeople.locotech.inspector.ui.activity.controlpoint

import ru.digipeople.locotech.inspector.ui.activity.controlpoint.adapter.ControlPoint
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс контрольных точек
 *
 * @author Kashonkov Nikita
 */
interface ControlPointView: MvpView {
    /**
     * Установка данных
     */
    fun setData(items: List<ControlPoint>)
    /**
     * Установка заголовка
     */
    fun setTitle(workName: String)
    /**
     * Установка названия секции
     */
    fun setSectionName(sectionName: String)
    /**
     * Управление видимостью лоадера
     */
    fun showLoading(isVisible: Boolean)
    /**
     * Отоюражение ошибки
     */
    fun showError(error: UserError)
}