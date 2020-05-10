package ru.digipeople.ui.delegate

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.fragment.dialog.WorkaroundNoticeDialog
import javax.inject.Inject

/**
 * Делегат для диалога
 */
@ActivityScope
class WorkaroundFragmentDelegate @Inject constructor(private val activity: AppCompatActivity) {

    private val workaroundNoticeDialog: WorkaroundNoticeDialog?
        get() = activity.supportFragmentManager.findFragmentByTag(WORKAROUND_NOTICE_DIALOG_FRAGMENT_TAG) as WorkaroundNoticeDialog?
    /**
     * Отображение диалога
     */
    fun show(onPositiveClick: () -> Unit, onNegativeClick: (() -> Unit)? = null) =
            workaroundNoticeDialog ?: WorkaroundNoticeDialog()
                    /**
                     * Обработка нажатия кнопок принять и отмена
                     */
                    .withListeners({
                        onPositiveClick.invoke()
                        hide()
                    }, {
                        onNegativeClick?.invoke()
                        hide()
                    })
                    .show(activity.supportFragmentManager, WORKAROUND_NOTICE_DIALOG_FRAGMENT_TAG)
    /**
     * Скрыть диалог
     */
    fun hide() = workaroundNoticeDialog?.dismiss()

    companion object {
        private const val WORKAROUND_NOTICE_DIALOG_FRAGMENT_TAG = "WORKAROUND_NOTICE_DIALOG_FRAGMENT_TAG"
    }
}