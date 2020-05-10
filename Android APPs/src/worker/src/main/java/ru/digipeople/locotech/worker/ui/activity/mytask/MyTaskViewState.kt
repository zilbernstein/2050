package ru.digipeople.locotech.worker.ui.activity.mytask

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.locotech.worker.model.Equipment
import javax.inject.Inject

/**
 * ViewState структуры работы
 *
 * @author Kashonkov Nikita
 */
class MyTaskViewState @Inject constructor() : BaseMvpViewState<MyTaskView>(), MyTaskView {
    override fun onViewAttached(view: MyTaskView?) {}

    override fun onViewDetached(view: MyTaskView?) {}
    /**
     * Загрузка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Equipment>) {
        forEachView { it.setDataToAdapter(list) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}