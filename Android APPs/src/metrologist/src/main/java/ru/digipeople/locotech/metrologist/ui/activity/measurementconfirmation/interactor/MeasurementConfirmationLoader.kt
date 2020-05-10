package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.measurementConfirmationMapper
import ru.digipeople.locotech.metrologist.data.model.Remark
import ru.digipeople.locotech.metrologist.data.model.WheelPairShort
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик замечаний
 */
class MeasurementConfirmationLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузить данные
     */
    fun load(measurementId: String): Single<Result> {
        return thingsWorxWorker.getMeasurementInfoBeforeComplete(measurementId)
                .map { response ->
                    /**
                     * Обработка результата
                     */
                    val model = measurementConfirmationMapper.entityToModel(response)!!
                    Result(UserError.NO_ERROR, model.wheelPairs, model.remarks)
                }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList(), emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val wheelPairs: List<WheelPairShort>,
                      val remarks: List<Remark>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}