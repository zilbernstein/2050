package ru.digipeople.locotech.master.ui.activity.measurementedit.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.response.IndicatorResponse
import ru.digipeople.locotech.master.model.Indicator
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

class MeasurementUploader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                              private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * загрузка измененнных замеров
     */
    fun editMeasure(workId: String, indicator: Indicator): Single<Result> = Single
            .fromCallable {
                IndicatorResponse(indicator.measurementId,
                        indicator.characteristicId,
                        indicator.measureStage,
                        indicator.measureValue,
                        indicator.comment)
            }
            .flatMap { thingsWorxWorker.editMeasure(workId, it) }
            .map { Result(UserError.NO_ERROR) }
            .onErrorReturn {
                /**
                 * отправка ошибки
                 */
                val userError = errorBuilder.fromThowable(it)
                Result(userError)
            }

    /**
     * стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}