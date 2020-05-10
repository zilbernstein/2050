package ru.digipeople.locotech.master.ui.activity.settings

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер настроек
 *
 * @author Kashonkov Nikita
 */
class SettingsPresenter @Inject constructor(state: SettingsViewState): BaseMvpViewStatePresenter<SettingsView, SettingsViewState>(state) {
    /**
     * Инициализация
     */
    override fun onInitialize() {}
}