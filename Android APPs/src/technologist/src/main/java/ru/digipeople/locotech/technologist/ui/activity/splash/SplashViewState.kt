package ru.digipeople.locotech.technologist.ui.activity.splash

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState для структуры сплэш
 *
 * @author Sostavkin Grisha
 */
class SplashViewState @Inject constructor() : BaseMvpViewState<SplashView>(), SplashView {
    override fun onViewAttached(view: SplashView?) {}

    override fun onViewDetached(view: SplashView?) {}
}