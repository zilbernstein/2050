package ru.digipeople.ui.fragment.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.digipeople.ui.UiComponent;
import ru.digipeople.ui.mvp.MvpDelegate;
import ru.digipeople.ui.mvp.view.MvpView;

/**
 * Базовый фрагмент с нормальной реализацией биндинга {@link MvpView}.
 *
 * @author Kashonkov Nikita
 */
public abstract class MvpFragment extends Fragment {

    private MvpDelegate mvpDelegate;
    /**
     * Флаг, что был вызван {@link #onSaveInstanceState(Bundle)}
     */
    private boolean mIsStateSaved;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpDelegate(UiComponent.Companion.get().mvpProcessor(), (MvpView) this);
        mvpDelegate.init(savedInstanceState);
    }
    /**
     * Конструктор
     */
    public MvpDelegate getMvpDelegate() {
        return mvpDelegate;
    }
    /**
     * Действия при старте
     */
    @Override
    public void onStart() {
        super.onStart();
        mvpDelegate.bindView();
    }
    /**
     * Действия при возобновлении
     */
    @Override
    public void onResume() {
        super.onResume();
        mIsStateSaved = false;
    }
    /**
     * Сохранение состояния
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.saveState(outState);
        mIsStateSaved = true;
    }
    /**
     * Действия при остановке
     */
    @Override
    public void onStop() {
        mvpDelegate.unbindView();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mvpDelegate.destroy(keepAlive());
        super.onDestroy();
    }

    protected boolean keepAlive() {
        boolean keepAlive = true;

        if (mIsStateSaved) {
            mIsStateSaved = false;
        } else {
            boolean anyParentIsRemoving = false;

            Fragment parent = getParentFragment();
            while (!anyParentIsRemoving && parent != null) {
                anyParentIsRemoving = parent.isRemoving();
                parent = parent.getParentFragment();
            }

            if (isRemoving() || anyParentIsRemoving || getActivity().isFinishing()) {
                keepAlive = false;
            }
        }
        return keepAlive;
    }
}
