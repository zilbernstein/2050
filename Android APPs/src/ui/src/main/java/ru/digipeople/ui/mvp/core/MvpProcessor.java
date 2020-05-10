package ru.digipeople.ui.mvp.core;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.digipeople.ui.mvp.PresenterProvider;
import ru.digipeople.ui.mvp.presenter.MvpPresenter;

/**
 * Класс, управляющий презентерами.
 *
 * @author Kashonkov Nikita
 */
@Singleton
public class MvpProcessor {

    private final PresenterStore presenterStore;
    private final PresenterCounter presenterCounter;
    /**
     * Конструктор
     */
    @Inject
    public MvpProcessor(PresenterStore presenterStore, PresenterCounter presenterCounter) {
        this.presenterStore = presenterStore;
        this.presenterCounter = presenterCounter;
    }
    /**
     * получить презентер
     */
    public <P extends MvpPresenter> P getPresenter(@NonNull PresenterProvider<P> presenterProvider, String tag) {
        P presenter = presenterStore.getPresenter(tag);
        if (presenter == null) {
            presenter = presenterProvider.providePresenter();
            presenterStore.putPresenter(tag, presenter);
        }
        presenterCounter.incrementCounter(tag);
        return presenter;
    }
    /**
     * освободить перезнтер
     */
    public <P extends MvpPresenter> void freePresenter(@NonNull P presenter, String tag, boolean keepAlive) {
        if (presenterCounter.decrementCounter(tag)) {
            if (!keepAlive) {
                presenterStore.removePresenter(tag);
                presenter.destroy();
            }
        }
    }

}
