package ru.digipeople.locotech.master.ui.activity.splash

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент экрана заставки
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface SplashComponent {

    fun inject(splashActivity: SplashActivity)

    fun splashPresenter(): SplashPresenter
}