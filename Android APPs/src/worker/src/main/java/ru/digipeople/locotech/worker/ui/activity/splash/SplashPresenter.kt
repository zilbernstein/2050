package ru.digipeople.locotech.worker.ui.activity.splash

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер для отображения сплэша
 *
 * @author Sostavkin Grisha
 */
class SplashPresenter @Inject constructor(splashViewState: SplashViewState): BaseMvpViewStatePresenter<SplashView, SplashViewState>(splashViewState) {
    override fun onInitialize() {}
}