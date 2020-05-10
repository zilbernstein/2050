package ru.digipeople.locotech.master.ui.activity.remark

import ru.digipeople.locotech.master.model.Remark
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**View state замечаний
 *
 * @author Kashonkov Nikita
 */
class RemarkViewState @Inject constructor() : BaseMvpViewState<RemarkView>(), RemarkView {
    private var isDataVisible = false

    override fun onViewAttached(view: RemarkView) {
        view.setDataVisibility(isDataVisible)
    }

    override fun onViewDetached(view: RemarkView?) {}
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Установка данных в адаптер работ
     */
    override fun setDataToWorkAdapter(list: List<Work>, shouldSavePosition: Boolean) {
        forEachView { it.setDataToWorkAdapter(list, shouldSavePosition) }
    }
    /**
     * Установка данных в адаптер замечаний
     */
    override fun setDataToRemarkAdapter(currentRemark: Remark, list: List<Remark>, shouldSavePosition: Boolean) {
        forEachView { it.setDataToRemarkAdapter(currentRemark, list, shouldSavePosition) }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        forEachView { it.setLoadingVisible(isVisible) }
    }
    /**
     * Установка прогресса выполнения
     */
    override fun setEquipmentProgress(progress: Int, leftTime: Long, requiredTime: Long) {
        forEachView { it.setEquipmentProgress(progress, leftTime, requiredTime) }
    }
    /**
     * Отобрадение диалогового окна
     */
    override fun showDeleteDialog() {
        forEachView { it.showDeleteDialog() }
    }
    /**
     * Управление видимостью данных
     */
    override fun setDataVisibility(isVisible: Boolean) {
        isDataVisible = isVisible
        forEachView { it.setDataVisibility(isVisible) }
    }
}