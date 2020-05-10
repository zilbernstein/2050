package ru.digipeople.locotech.technologist.ui.activity.splash

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Сплэш презентер
 *
 * @author Sostavkin Grisha
 */
class SplashPresenter @Inject constructor(splashViewState: SplashViewState): BaseMvpViewStatePresenter<SplashView, SplashViewState>(splashViewState) {
    override fun onInitialize() {}
}