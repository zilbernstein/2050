package ru.digipeople.locotech.worker.ui.activity.task

import ru.digipeople.locotech.worker.model.MeasurementStatus
import ru.digipeople.locotech.worker.model.MeasurementsTask
import ru.digipeople.locotech.worker.model.WorkDetail
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState структуры задания
 *
 * @author Kashonkov Nikita
 */
class TaskViewState @Inject constructor() : BaseMvpViewState<TaskView>(), TaskView {
    private var isShowingBlank = false

    override fun onViewAttached(view: TaskView) {
        view.showBlankView(isShowingBlank)
    }

    override fun onViewDetached(view: TaskView?) {}

    override fun setUpTaskView(workDetail: WorkDetail) {
        forEachView { it.setUpTaskView(workDetail) }
    }
    /**
     * показать ошибку
     */
    override fun showMistake(message: String) {
        forEachView { it.showMistake(message) }
    }
    /**
     * управление видимсотью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * ошибка не начата работа
     */
    override fun showStartShiftMistake() {
        forEachView { it.showStartShiftMistake() }
    }
    /**
     * показатб пустой экран
     */
    override fun showBlankView(isVisible: Boolean) {
        this.isShowingBlank = isVisible
        forEachView { it.showBlankView(isVisible) }
    }
    /**
     * запрос на получение аппаратных замеров
     */
    override fun setMeasurementsTask(task: MeasurementsTask) {
        forEachView { it.setMeasurementsTask(task) }
    }
    /**
     * установка статуса
     */
    override fun setMeasurementsStatus(status: MeasurementStatus) {
        forEachView { it.setMeasurementsStatus(status) }
    }
    /**
     * ошибка чеклиста
     */
    override fun showChecklistError() {
        forEachView { it.showChecklistError() }
    }
    /**
     * ошибка при выборе чеклиста
     */
    override fun showChecklistCheckError() {
        forEachView { it.showChecklistCheckError() }
    }
}