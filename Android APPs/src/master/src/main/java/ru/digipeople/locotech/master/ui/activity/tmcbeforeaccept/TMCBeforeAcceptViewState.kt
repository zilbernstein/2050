package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept

import ru.digipeople.locotech.master.model.WriteOffTmc
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 *  View state списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCBeforeAcceptViewState @Inject constructor(): BaseMvpViewState<TMCBeforeAcceptView>(), TMCBeforeAcceptView {
    override fun onViewAttached(view: TMCBeforeAcceptView?) {}

    override fun onViewDetached(view: TMCBeforeAcceptView?) { }
    /**
     * Установка данных
     */
    override fun setData(list: List<WriteOffTmc>) {
        forEachView { it.setData(list) }
    }
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
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * Диалог списания ТМЦ
     */
    override fun showWriteOffDialog(shouldShowOverrunWarning: Boolean) {
        forEachView { it.showWriteOffDialog(shouldShowOverrunWarning) }
    }
}