package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.measurementInfoMapper
import ru.digipeople.locotech.metrologist.data.model.MeasurementInfo
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик детальной информации замера
 */
class MeasurementLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                            private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка данных
     */
    fun load(categoryId: String): Single<Result> {
        return thingsWorxWorker.getMeasurementInfo(categoryId)
                .map { response ->
                    /**
                     * Обработка результата
                     */
                    Result(UserError.NO_ERROR, measurementInfoMapper.fromEntity(response))
                }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError,
                 private val _measurementInfo: MeasurementInfo?) {
        val measurementInfo
            get() = _measurementInfo!!
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}
