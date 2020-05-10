package ru.digipeople.ui.activity.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.digipeople.ui.mvp.MvpDelegate;
import ru.digipeople.ui.mvp.view.MvpView;

/**
 * Базовый диалог фрагмент с нормальной реализацией биндинга {@link MvpView}.
 *
 * @author Kashonkov Nikita
 */
public abstract class MvpDialogFragment extends BaseDialogFragment {

    private MvpDelegate mvpDelegate;
    /**
     * Флаг, что был вызван {@link #onSaveInstanceState(Bundle)}
     */
    private boolean mIsStateSaved;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpDelegate(appComponent().mvpProcessor(), (MvpView) this);
        mvpDelegate.init(savedInstanceState);
    }
    /**
     * конструктов
     */
    public MvpDelegate getMvpDelegate() {
        return mvpDelegate;
    }
    /**
     * действия при старте активити
     */
    @Override
    public void onStart() {
        super.onStart();
        mvpDelegate.bindView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsStateSaved = false;
    }
    /**
     * сохранения состояния
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.saveState(outState);
        mIsStateSaved = true;
    }
    /**
     * действия при остановке делегата
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
