package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.wheelPairsMapper
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик колесных пар
 *
 * @author Michael Solenov
 */
class WheelPairsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                           private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка данных
     */
    fun load(measurementId: String): Single<Result> {
        return thingsWorxWorker.getWheelPairs(measurementId)
                .map { response ->
                    /**
                     * Обработка результата
                     */
                    Result(UserError.NO_ERROR, wheelPairsMapper.entityListToModelList(response.entityList))
                }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
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