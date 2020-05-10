package ru.digipeople.locotech.inspector.ui.activity.measurement.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.data.api.mapper.MeasurementMapper
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик замеров
 *
 * @author Sostavkin Grisha
 */
class MeasurementsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    private val measurementMapper = MeasurementMapper.INSTANCE
    /**
     * Загрузка замеров
     */
    fun loadMeasurements(workId: String): Single<Result> {
        return thingsWorxWorker.getWorkMeasurements(workId)
                .map { response ->
                    /**
                     * обработка результата
                     */
                    Result(UserError.NO_ERROR,
                            measurementMapper.entityListToModelList(response.entityList))
                }
                .onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val items: List<Measurement>) {
        val isSuccessful
            get() = userError === UserError.NO_ERROR
    }

}