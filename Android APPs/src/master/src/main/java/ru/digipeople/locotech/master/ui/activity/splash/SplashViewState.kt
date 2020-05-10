package ru.digipeople.locotech.master.ui.activity.splash

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * Vuew State экрана заставки
 *
 * @author Kashonkov Nikita
 */
class SplashViewState @Inject constructor(): BaseMvpViewState<SplashView>(), SplashView {
    override fun onViewAttached(view: SplashView?) {}

    override fun onViewDetached(view: SplashView?) {}
}