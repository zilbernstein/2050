package ru.digipeople.ui.mvp.core;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import ru.digipeople.ui.mvp.presenter.MvpPresenter;

/**
 * Хранилище презентеров.
 *
 * @author Kashonkov Nikita
 */
public class PresenterStore {

    private Map<String, MvpPresenter> presenters = new HashMap<>();
    /**
     * конструктор
     */
    @Inject
    public PresenterStore() {

    }
    /**
     * Получить презентер
     */
    public <P extends MvpPresenter> P getPresenter(@NonNull String tag) {
        return (P) presenters.get(tag);
    }

    public void putPresenter(@NonNull String tag, @NonNull MvpPresenter presenter) {
        presenters.put(tag, presenter);
    }
    /**
     * Удалить презентер
     */
    public void removePresenter(@NonNull String tag) {
        presenters.remove(tag);
    }
}
