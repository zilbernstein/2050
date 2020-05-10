package ru.digipeople.ui.mvp.viewState;

import java.util.ArrayList;
import java.util.List;

import ru.digipeople.ui.mvp.view.MvpView;

/**
 * Скелетная реализация {@link MvpViewState}.
 *
 * @param <V> {@link MvpView}
 * @author Kashonkov Nikita
 */
public abstract class BaseMvpViewState<V extends MvpView> implements MvpViewState<V> {

    private final List<V> views = new ArrayList<>();

    @Override
    public void attachView(V view) {
        views.add(view);
        onViewAttached(view);
    }

    @Override
    public void detachView(V view) {
        views.remove(view);
        onViewDetached(view);
    }

    /**
     * Колбэк при присоединении view.
     *
     * @param view Присоединенная view
     */
    protected abstract void onViewAttached(V view);

    /**
     * Колбэк при отсоединении view.
     *
     * @param view Отсоединенная view
     */
    protected abstract void onViewDetached(V view);

    /**
     * Выполняет операцию на всех привязанных view.
     *
     * @param action Операция для выполнения
     */
    protected void forEachView(Consumer<V> action) {
        for (V view : views) {
            action.accept(view);
        }
    }

    /**
     * Операция, выполняемая на view.
     *
     * @param <T> Тип view {@link MvpView}
     */
    public interface Consumer<T> {

        /**
         * Выполняет операцию на view.
         *
         * @param view {@link MvpView}
         */
        void accept(T view);
    }
}
