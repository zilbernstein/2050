package ru.digipeople.locotech.inspector.ui.activity.splash

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер экрана заставки
 *
 * @author Kashonkov Nikita
 */
class SplashPresenter @Inject constructor(splashViewState: SplashViewState) : BaseMvpViewStatePresenter<SplashView, SplashViewState>(splashViewState) {
    override fun onInitialize() {}
}