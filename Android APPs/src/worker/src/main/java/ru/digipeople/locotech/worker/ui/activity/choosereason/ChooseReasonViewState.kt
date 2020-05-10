package ru.digipeople.locotech.worker.ui.activity.choosereason

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.locotech.worker.model.PauseReason
import javax.inject.Inject

/**
 * ViewState выбора причины остановки работы
 *
 * @author Kashonkov Nikita
 */
class ChooseReasonViewState @Inject constructor() : BaseMvpViewState<ChooseReasonView>(), ChooseReasonView {
    //region Other
    var reasonId: String? = null
    var returnResultFlag = false
    //endRegion
    override fun onViewAttached(view: ChooseReasonView?) {
        if (reasonId != null) {
            view!!.finishActivityWithResult(reasonId!!)
        }
    }

    override fun onViewDetached(view: ChooseReasonView?) {}
    /**
     * Установка данных
     */
    override fun setData(list: List<PauseReason>) {
        forEachView { it.setData(list) }
    }
    /**
     * Заверщение активити с результатом
     */
    override fun finishActivityWithResult(reasonId: String) {
        this.reasonId = reasonId
        forEachView { it.finishActivityWithResult(reasonId) }
    }
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