package ru.digipeople.locotech.master.ui.activity.settings

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 *  View State настроек
 *
 * @author Kashonkov Nikita
 */
class SettingsViewState @Inject constructor(): BaseMvpViewState<SettingsView>(), SettingsView {
    override fun onViewAttached(view: SettingsView?) {}

    override fun onViewDetached(view: SettingsView?) {}
}