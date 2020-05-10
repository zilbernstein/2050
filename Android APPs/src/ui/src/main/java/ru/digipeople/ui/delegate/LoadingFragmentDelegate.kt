package ru.digipeople.ui.delegate

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.fragment.dialog.LoadingDialog
import javax.inject.Inject

/**
 * Делегат для отображения лоадера
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
class LoadingFragmentDelegate @Inject constructor(private val activity: AppCompatActivity) {
    private val loadingDialog: LoadingDialog?
        get() {
            return activity.supportFragmentManager
                    .findFragmentByTag(LOADING_DIALOG_FRAGMENT_TAG) as LoadingDialog?
        }
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean) {
        if (isVisible) showLoading() else hideLoading()
    }
    /**
     * Отображение загрузки
     */
    private fun showLoading() {
        val prevDialog = loadingDialog
        if (prevDialog != null) {
            with(activity.supportFragmentManager.beginTransaction()) {
                remove(prevDialog)
            }.commit()
        }
        LoadingDialog().show(activity.supportFragmentManager, LOADING_DIALOG_FRAGMENT_TAG)
    }

    /**
     * Скрыть загрузку
     */
    private fun hideLoading() = loadingDialog?.dismiss()

    companion object {
        private const val LOADING_DIALOG_FRAGMENT_TAG = "LOADING_DIALOG_FRAGMENT_TAG"
    }
}