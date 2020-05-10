package ru.digipeople.locotech.master.ui.activity.status

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер статуса работ
 *
 * @author Kashonkov Nikita
 */
class StatusPresenter @Inject constructor(state: StatusViewState) : BaseMvpViewStatePresenter<StatusView, StatusViewState>(state) {
    /**
     * Инициализация
     */
    override fun onInitialize() {}
}