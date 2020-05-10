package ru.digipeople.locotech.worker.ui.activity.shift

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.locotech.worker.model.Client
import javax.inject.Inject

/**
 * ViewState смены
 *
 * @author Kashonkov Nikita
 */
class ShiftViewState @Inject constructor() : BaseMvpViewState<ShiftView>(), ShiftView {
    override fun onViewAttached(view: ShiftView?) {}

    override fun onViewDetached(view: ShiftView?) {}
    /**
     * Установка данных
     */
    override fun setUpView(client: Client, isInWork: Boolean) {
        forEachView { it.setUpView(client, isInWork) }
    }
    /**
     * Отображение статуса
     */
    override fun showWorkStatus(isInWork: Boolean) {
        forEachView { it.showWorkStatus(isInWork) }
    }
    /**
     * Управление видимостью лодера
     */
    override fun setLoading(isLoading: Boolean) {
        forEachView { it.setLoading(isLoading) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
}