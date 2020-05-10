package ru.digipeople.locotech.master.ui.activity.settings

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент настроек
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface SettingsComponent {

    fun inject(settingsActivity: SettingsActivity)

    fun settingsPresenter(): SettingsPresenter
}