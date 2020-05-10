package ru.digipeople.ui.activity.base;

import androidx.fragment.app.DialogFragment;
import ru.digipeople.ui.UiComponent;

/**
 * Базовый класс для всех {@link DialogFragment} приложения.
 *
 * @author Kashonkov Nikita
 */
public class BaseDialogFragment extends DialogFragment {

    protected final UiComponent appComponent() {
        return UiComponent.Companion.get();
    }
}
