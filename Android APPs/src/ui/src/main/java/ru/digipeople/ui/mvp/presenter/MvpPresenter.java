package ru.digipeople.ui.mvp.presenter;

import androidx.annotation.NonNull;

import ru.digipeople.ui.mvp.view.MvpView;

/**
 * MVP презентер.
 *
 * @param <V> {@link MvpView}, c которой работает презентер.
 * @author Kashonkov Nikita
 */
public interface MvpPresenter<V extends MvpView> {
    /**
     * Привязывает {@link MvpView} к презентеру
     *
     * @param view Представление
     */
    void bind(@NonNull V view);

    /**
     * Отвязывает {@link MvpView} от презентера
     *
     * @param view
     */
    void unbind(@NonNull V view);

    /**
     * Вызывается при очищении ссылки на презентер.
     * Последняя точка, в которой нужно отвязаться от всех static ссылок.
     */
    void destroy();
}
