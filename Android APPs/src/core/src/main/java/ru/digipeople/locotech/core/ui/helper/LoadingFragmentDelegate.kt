package ru.digipeople.locotech.core.ui.helper

import androidx.fragment.app.Fragment
import ru.digipeople.ui.fragment.dialog.LoadingDialog

/**
 * Делегат для фрагмента загрузки
 */
class LoadingFragmentDelegate constructor(val fragment: Fragment) {
    private val loadingDialog = { fragment: Fragment ->
        fragment.childFragmentManager
                .findFragmentByTag(LOADING_DIALOG_FRAGMENT_TAG) as LoadingDialog?
    }
    /**
     * Управление видимостью загрузки
     */
    fun setLoadingVisibility(isVisible: Boolean) {
        if (isVisible) this.showLoading() else this.hideLoading()
    }
    /**
     * Показатьь загрузку
     */
    @Deprecated("use setLoadingVisibility instead")
    fun showLoading() = loadingDialog.invoke(fragment)
            ?: LoadingDialog().show(fragment.childFragmentManager, LOADING_DIALOG_FRAGMENT_TAG)

    /**
     * Скрыть загрузку
     */
    @Deprecated("use setLoadingVisibility instead")
    fun hideLoading() = loadingDialog.invoke(fragment)?.dismiss()


    companion object {
        private const val LOADING_DIALOG_FRAGMENT_TAG = "LOADING_DIALOG_FRAGMENT_TAG"
    }
}