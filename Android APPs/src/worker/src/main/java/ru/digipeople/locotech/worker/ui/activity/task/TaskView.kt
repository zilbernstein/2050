package ru.digipeople.locotech.worker.ui.activity.task

import ru.digipeople.locotech.worker.model.MeasurementStatus
import ru.digipeople.locotech.worker.model.MeasurementsTask
import ru.digipeople.locotech.worker.model.WorkDetail
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс структуры задания
 *
 * @author Kashonkov Nikita
 */
interface TaskView : MvpView {
    /**
     * установк данных
     */
    fun setUpTaskView(workDetail: WorkDetail)
    /**
     * сообщение об ошибке
     */
    fun showMistake(message: String)
    /**
     * ошибка при старе
     */
    fun showStartShiftMistake()
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * тображение пустого экрана
     */
    fun showBlankView(isVisible: Boolean)
    /**
     * отправка запроса на получение апп замеров
     */
    fun setMeasurementsTask(task: MeasurementsTask)
    /**
     * установка статуса
     */
    fun setMeasurementsStatus(status: MeasurementStatus)
    /**
     * ошибка чеклиста
     */
    fun showChecklistError()
    /**
     * ошибка выюора в чеклисте
     */
    fun showChecklistCheckError()
}