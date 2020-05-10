package ru.digipeople.locotech.worker.ui.activity.measurements.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.data.api.mapper.MeasurementMapper
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.MeasurementStatus
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загружает замеры
 *
 * @author Mike Solenov
 */
class MeasurementsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппреа
     */
    private val measurementMapper = MeasurementMapper.INSTANCE
    /**
     * Загрузка замеров
     */
    fun loadMeasurements(workId: String): Single<Result> {
        return thingsWorxWorker.getWorkMeasurements(workId)
                .map { response ->
                    /**
                     * Обработка результата
                     */
                    val measurements = measurementMapper.entityListToModelList(response.entityList)
                    val measurementsByStage = mapOf<Stage, MutableList<Measurement>>(
                            Stage.BEFORE_REPAIR to mutableListOf(),
                            Stage.AFTER_REPAIR to mutableListOf(),
                            Stage.CONTROL to mutableListOf()
                    )
                    for (measurement in measurements)
                        measurementsByStage[measurement.stage]?.add(measurement)

                    Result(UserError.NO_ERROR, measurementsByStage, MeasurementStatus.NO_TASK)
                }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    if (userError.message == errorBuilder.unknownError.message) {
                        // Хак по запросу Ковалевского, потому что некому сейчас починить сервер
                        return@onErrorReturn Result(UserError.NO_ERROR, emptyMap(), MeasurementStatus.NO_TASK)
                    }
                    Result(userError, emptyMap(), MeasurementStatus.NO_TASK)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val measurements: Map<Stage, List<Measurement>>,
                      val measurementStatus: MeasurementStatus) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}
