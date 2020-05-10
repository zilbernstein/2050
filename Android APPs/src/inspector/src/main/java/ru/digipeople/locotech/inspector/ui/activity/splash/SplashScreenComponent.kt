package ru.digipeople.locotech.inspector.ui.activity.splash

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент экрана заставки
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface SplashScreenComponent {

    fun splashComponent(activityModule: ActivityModule): SplashComponent
}