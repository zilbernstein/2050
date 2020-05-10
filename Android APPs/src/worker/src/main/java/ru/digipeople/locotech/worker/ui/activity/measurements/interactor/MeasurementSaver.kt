package ru.digipeople.locotech.worker.ui.activity.measurements.interactor

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.MeasurementChangeRequest
import ru.digipeople.locotech.worker.ui.activity.measurements.adapter.EditableMeasurement
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Отправляет информацию по замеры
 */
class MeasurementSaver @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {/**
 * Созранение списка замеров
 */
    fun save(workId: String, editableMeasurements: List<EditableMeasurement>): Single<Result> {
        val requests = editableMeasurements.map {
            val measurement = it.measurement
            /**
             * Необходимые показатели замера
             */
            MeasurementChangeRequest(
                    measurementId = measurement.measurementId,
                    characteristicId = measurement.characteristicId,
                    measurementStage = measurement.stage.value,
                    measurementValue = it.value,
                    comment = it.comment
            )
        }
        return thingsWorxWorker.postChangeMeasurement(workId, requests)
                .map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}

