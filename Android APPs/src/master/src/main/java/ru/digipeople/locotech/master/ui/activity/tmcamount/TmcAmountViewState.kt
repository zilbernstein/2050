package ru.digipeople.locotech.master.ui.activity.tmcamount

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcAmountViewState @Inject constructor(): BaseMvpViewState<TmcAmountView>(), TmcAmountView {
    private var isLoadingVisible = false

    override fun onViewAttached(view: TmcAmountView) {
        setLoadingVisibility(isLoadingVisible)
    }

    override fun onViewDetached(view: TmcAmountView) { }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }

}