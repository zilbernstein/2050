package ru.digipeople.locotech.inspector.ui.activity.measurement

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс замеров
 *
 * @author Sostavkin Grisha
 */
interface MeasurementView : MvpView {
    fun setData(items: List<Any>)
    fun setLoading(isVisible: Boolean)
    fun showError(message: String)
}