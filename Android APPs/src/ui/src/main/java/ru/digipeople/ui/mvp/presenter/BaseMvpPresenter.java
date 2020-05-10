package ru.digipeople.ui.mvp.presenter;

import androidx.annotation.NonNull;

import ru.digipeople.ui.mvp.view.MvpView;

/**
 * Скелетная реализация {@link MvpPresenter}.
 *
 * @param <V> {@link MvpView}, c которой работает презентер.
 * @author Kashonkov Nikita
 */
public abstract class BaseMvpPresenter<V extends MvpView> implements MvpPresenter<V> {

    /**
     * Представление, ассоциированное с презентером.
     */
    protected V view;
    /**
     * Флаг, что презентер проинициализирован, т.е. начал свою работу
     */
    private boolean initialized = false;
    /**
     * Инициализация
     */
    public void initialize() {
        if (!initialized) {
            initialized = true;
            onInitialize();
        }
    }

    protected abstract void onInitialize();
    /**
     * Проверка инициализации
     */
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void bind(@NonNull V view) {
        this.view = view;
    }

    @Override
    public void unbind(@NonNull V view) {
        this.view = null;
    }
    /**
     * Уничтожение презентера
     */
    @Override
    public void destroy() {

    }
}
