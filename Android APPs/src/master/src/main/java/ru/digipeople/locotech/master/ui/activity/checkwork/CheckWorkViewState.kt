package ru.digipeople.locotech.master.ui.activity.checkwork

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
class CheckWorkViewState @Inject constructor(): BaseMvpViewState<CheckWorkView>(), CheckWorkView {
    override fun onViewAttached(view: CheckWorkView?) {}

    override fun onViewDetached(view: CheckWorkView?) {}
    /**
     * установка данных
     */
    override fun setData(list: List<Work>) { forEachView { it.setData(list) } }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) { forEachView { it.showError(message) } }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        forEachView { it.setLoadingVisible(isVisible) }
    }
}