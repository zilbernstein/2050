package ru.digipeople.locotech.inspector.ui.activity.controlpoint

import ru.digipeople.locotech.inspector.ui.activity.controlpoint.adapter.ControlPoint
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Вью стейт контрольных точек
 *
 * @author Kashonkov Nikita
 */
class ControlPointViewState @Inject constructor() : BaseMvpViewState<ControlPointView>(), ControlPointView {

    override fun onViewAttached(view: ControlPointView) {}

    override fun onViewDetached(view: ControlPointView) {}
    /**
     * Установка данных
     */
    override fun setData(items: List<ControlPoint>) {
        forEachView { it.setData(items) }
    }
    /**
     * Установка заголовка
     */
    override fun setTitle(workName: String) {
        forEachView { it.setTitle(workName) }
    }
    /**
     * Установка названия секции
     */
    override fun setSectionName(SectionName: String) {
        forEachView { it.setSectionName(SectionName) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun showLoading(isVisible: Boolean) {
        forEachView { it.showLoading(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        forEachView { it.showError(error) }
    }
}