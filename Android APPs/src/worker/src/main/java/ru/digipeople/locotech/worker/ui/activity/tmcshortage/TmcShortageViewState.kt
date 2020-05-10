package ru.digipeople.locotech.worker.ui.activity.tmcshortage

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.locotech.worker.model.TMCInWork
import javax.inject.Inject

/**
 * ViewState сттруктуры недостающие ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcShortageViewState @Inject constructor() : BaseMvpViewState<TmcShortageView>(), TmcShortageView {
    //region Other
    var returnResultFlag = false
    var loadingVisible = false
    //endRegion
    override fun onViewAttached(view: TmcShortageView) {
        if (returnResultFlag) {
            view!!.finishActivityWithResult()
        }
        view.setLoadingVisibility(loadingVisible)
    }

    override fun onViewDetached(view: TmcShortageView) {
    }
    /**
     * Обновление данных в адаптере
     */
    override fun updateAdapter() {
        forEachView { it.updateAdapter() }
    }
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(allList: List<TMCInWork>, currentList: MutableList<TMCInWork>) {
        forEachView { it.setDataToAdapter(allList, currentList) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        this.loadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * Запрос причины остановки
     */
    override fun showCheckMistake() {
        forEachView { it.showCheckMistake() }
    }
    /**
     * Отображенгие ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }

    override fun finishActivityWithResult() {
        returnResultFlag = true
        forEachView { it.finishActivityWithResult() }
    }
}