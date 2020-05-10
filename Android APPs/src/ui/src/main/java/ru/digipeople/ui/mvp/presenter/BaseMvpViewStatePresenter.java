package ru.digipeople.ui.mvp.presenter;

import androidx.annotation.NonNull;

import ru.digipeople.ui.mvp.view.MvpView;
import ru.digipeople.ui.mvp.viewState.MvpViewState;

/**
 * Скелетная реализация {@link MvpPresenter, работающего с прослойкой {@link MvpViewState }.
 *
 * @param <V>  {@link MvpView }, c которой работает презентер.
 * @param <VS> {@link MvpViewState}, c которой работает презентер.
 * @author Kashonkov Nikita
 */
public abstract class BaseMvpViewStatePresenter<V extends MvpView, VS extends MvpViewState<V>> implements MvpPresenter<V> {

    /**
     * Представление, ассоциированное с презентером.
     */
    protected final V view;
    /**
     * {@link MvpViewState} прослойка
     */
    private final VS viewState;
    /**
     * Флаг, что презентер проинициализирован, т.е. начал свою работу
     */
    private boolean initialized = false;

    public BaseMvpViewStatePresenter(VS viewState) {
        this.viewState = viewState;
        this.view = (V) viewState;
    }
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
        viewState.attachView(view);
    }

    @Override
    public void unbind(@NonNull V view) {
        viewState.detachView(view);
    }
    /**
     * Уничтожение презентера
     */
    @Override
    public void destroy() {
    }
}
