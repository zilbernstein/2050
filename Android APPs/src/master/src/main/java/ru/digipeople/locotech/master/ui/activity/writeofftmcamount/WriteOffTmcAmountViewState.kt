package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * view state списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
class WriteOffTmcAmountViewState @Inject constructor(): BaseMvpViewState<WriteOffTTmcAmountView>(), WriteOffTTmcAmountView {
    override fun onViewAttached(view: WriteOffTTmcAmountView?) {}

    override fun onViewDetached(view: WriteOffTTmcAmountView?) {}
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
      forEachView { it.showError(message) }
    }
}