package ru.digipeople.ui.mvp.viewState;

import ru.digipeople.ui.mvp.view.MvpView;

/**
 * Состояние {@link MvpView}.
 *
 * @param <V> {@link MvpView}
 * @author Kashonkov Nikita
 */
public interface MvpViewState<V extends MvpView> {

    /**
     * Присоединение view.
     *
     * @param view Присоединенная view
     */
    void attachView(V view);

    /**
     * Отсоединение view.
     *
     * @param view Отсоединенная view
     */
    void detachView(V view);
}
