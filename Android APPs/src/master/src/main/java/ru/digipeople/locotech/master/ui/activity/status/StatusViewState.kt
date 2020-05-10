package ru.digipeople.locotech.master.ui.activity.status

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state статуса работ
 *
 * @author Kashonkov Nikita
 */
class StatusViewState @Inject constructor(): BaseMvpViewState<StatusView>(), StatusView {
    override fun onViewAttached(view: StatusView?) {}

    override fun onViewDetached(view: StatusView?) {}
}