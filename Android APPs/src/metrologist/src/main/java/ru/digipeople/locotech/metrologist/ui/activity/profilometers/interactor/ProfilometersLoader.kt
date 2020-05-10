package ru.digipeople.locotech.metrologist.ui.activity.profilometers.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.measurementMapper
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик профилометров
 */
class ProfilometersLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                              private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка списка профилометров
     */
    fun loadProfilometerMeasurements(): Single<Result> {
        return thingsWorxWorker.getProfilometerMeasurements()
                .map { measurementMapper.fromEntityList(it.entityList) }
                .map { addProfilometersToList(it) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyMap())
                }
    }
    /**
     * Обработка результата
     */
    fun addProfilometersToList(measurements: List<Measurement>): Map<String, List<Measurement>> {
        val addedProfilometers = measurements.groupBy { it.profilometerNumber }
        return addedProfilometers
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val measurements: Map<String, List<Measurement>>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}