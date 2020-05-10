package ru.digipeople.locotech.master.ui.activity.urgent

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * view state срочно
 *
 * @author Kashonkov Nikita
 */
class UrgentViewState @Inject constructor() : BaseMvpViewState<UrgentView>(), UrgentView {
    override fun onViewAttached(view: UrgentView?) {}

    override fun onViewDetached(view: UrgentView?) {}
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Work>) {
        forEachView { it.setDataToAdapter(list) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        forEachView { it.setLoadingVisible(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
       forEachView { it.showError(message) }
    }
}