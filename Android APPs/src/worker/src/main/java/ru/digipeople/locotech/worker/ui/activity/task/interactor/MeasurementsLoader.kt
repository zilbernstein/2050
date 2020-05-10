package ru.digipeople.locotech.worker.ui.activity.task.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.data.api.mapper.MeasurementMapper
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.MeasurementStatus
import ru.digipeople.locotech.worker.model.mapper.measurementStatusMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик готовности аппаратных замеров
 */
class MeasurementsLoader @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * создание маппера
     */
    private val measurementMapper = MeasurementMapper.INSTANCE
    /**
     * Загрузка замеров по заданию
     */
    fun loadMeasurementsByTask(workId: String, taskId: String, stage: Stage): Single<Result> {
        return thingsWorxWorker.checkMeasurementsReady(workId, taskId, stage.value)
                .map { response ->
                    /**
                     * обработка результата
                     */
                    val status = measurementStatusMapper.fromEntity(response.measurementsStatus)
                            ?: MeasurementStatus.NO_TASK
                    val measurements = measurementMapper.entityListToModelList(response.measurements)
                    Result(UserError.NO_ERROR, measurements, status)
                }.onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList(), MeasurementStatus.NO_TASK)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val items: List<Measurement>,
                      val measurementStatus: MeasurementStatus) {
        val isSuccessful
            get() = userError === UserError.NO_ERROR
    }
}