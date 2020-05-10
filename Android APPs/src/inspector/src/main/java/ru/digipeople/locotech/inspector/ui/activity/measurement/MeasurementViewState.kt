package ru.digipeople.locotech.inspector.ui.activity.measurement

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State замеров
 *
 * @author Sostavkin Grisha
 */
class MeasurementViewState @Inject constructor() : BaseMvpViewState<MeasurementView>(), MeasurementView {
    private var isLoadingVisible = false

    override fun onViewAttached(view: MeasurementView) {
        view.setLoading(isLoadingVisible)
    }

    override fun onViewDetached(view: MeasurementView) {}

    override fun setData(items: List<Any>) {
        forEachView { it.setData(items) }
    }

    override fun setLoading(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoading(isVisible) }
    }

    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}