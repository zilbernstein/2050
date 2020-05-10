package ru.digipeople.locotech.master.ui.activity.settings

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент настроек
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface SettingsScreenComponent {

    fun settingsComponent(activityModule: ActivityModule): SettingsComponent
}