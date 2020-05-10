package ru.digipeople.locotech.master.ui.activity.splash

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

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