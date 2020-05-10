package ru.digipeople.ui.mvp;

import ru.digipeople.ui.mvp.presenter.MvpPresenter;

/**
 * Фабрика презентеров.
 *
 * @author Kashonkov Nikita
 */
public interface PresenterProvider<P extends MvpPresenter> {
    P providePresenter();
}
