package ru.digipeople.locotech.worker.ui.activity.splash

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры сплэш
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface SplashComponent {

    fun inject(splashActivity: SplashActivity)

    fun splashPresenter(): SplashPresenter
}