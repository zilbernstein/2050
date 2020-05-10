package ru.digipeople.locotech.master.ui.activity.partlyaccept

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state частичной приемки
 *
 * @author Kashonkov Nikita
 */
class PartlyAcceptViewState @Inject constructor() : BaseMvpViewState<PartlyAcceptView>(), PartlyAcceptView {
    private var model: Work? = null
    private var isLoadingVisible = false

    override fun onViewAttached(view: PartlyAcceptView) {
        model?.let { view.showModel(it) }
        view.setLoadingVisibility(isLoadingVisible)
    }

    override fun onViewDetached(view: PartlyAcceptView?) {}
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        isLoadingVisible = isVisible
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * отображение данных по частичной приемке
     */
    override fun showModel(model: Work?) {
        this.model = model
        forEachView { it.showModel(model) }
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * установка процентов
     */
    override fun setPercent(percent: String) {
        forEachView { it.setPercent(percent) }
    }
    /**
     * установка новых процентов
     */
    override fun setNewOutfitPercent(percent: Int) {
        forEachView { it.setNewOutfitPercent(percent) }
    }
    /**
     * установка нового времени
     */
    override fun setNewOutfitTime(time: Long) {
        forEachView { it.setNewOutfitTime(time) }
    }
    /**
     * управление видимостью информациии
     */
    override fun setNewOutfitInfoVisibility(isVisible: Boolean) {
        forEachView { it.setNewOutfitInfoVisibility(isVisible) }
    }
    /**
     * управление доступностью кнопки
     */
    override fun setAcceptButtonEnability(isEnable: Boolean) {
        forEachView { it.setAcceptButtonEnability(isEnable) }
    }
}