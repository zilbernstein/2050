package ru.digipeople.locotech.metrologist.ui.activity.editmeasurement.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.wheelPairsMapper
import ru.digipeople.locotech.metrologist.data.api.mapper.wheelParamMapper
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.locotech.metrologist.data.model.WheelParam
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * @author Michael Solenov
 */
class PairParamsSaver @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Сохранение данных
     */
    fun save(pairId: String, wheelParams: List<WheelParam<*>>): Single<Result> = Single
            .fromCallable { wheelParams.map { wheelParamMapper.toWheelParamsValue(it)!! } }
            .flatMap { thingsWorxWorker.setPairParamsValues(pairId, it) }
            .map { response ->
                /**
                 * Обработка результата
                 */
                Result(UserError.NO_ERROR, wheelPairsMapper.entityListToModelList(response.entityList))
            }
            .onErrorReturn {
                val userError = errorBuilder.fromThowable(it)
                Result(userError, emptyList())
            }

    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val wheelPairs: List<WheelPair>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}