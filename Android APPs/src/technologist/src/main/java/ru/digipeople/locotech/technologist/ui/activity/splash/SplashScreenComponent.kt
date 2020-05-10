package ru.digipeople.locotech.technologist.ui.activity.splash

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры сплэш
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface SplashScreenComponent {

    fun splashComponent(activityModule: ActivityModule): SplashComponent
}