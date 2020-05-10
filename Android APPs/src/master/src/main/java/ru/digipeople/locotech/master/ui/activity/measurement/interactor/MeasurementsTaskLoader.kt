package ru.digipeople.locotech.master.ui.activity.measurement.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.MeasurementsTask
import ru.digipeople.locotech.master.model.mapper.measurementsTaskMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

class MeasurementsTaskLoader @Inject constructor(
        private val thingWorxWorker: ThingsWorxWorker,
        private val userErrorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * получения задания на аппаратный замер
     */
    fun getMeasurementsTask(workId: String, stage: Stage, sectionNumber: String): Single<Result> = thingWorxWorker
            .getMeasurementsTask(workId, sectionNumber, stage.value)
            .map { response ->
                /**
                 * обработка результата
                 */
                val model = measurementsTaskMapper.fromEntity(response)
                model?.measurementsStage = stage
                Result(UserError.NO_ERROR, model)
            }
            .onErrorReturn {
                /**
                 * отображение ошибки
                 */
                val userError = userErrorBuilder.fromThowable(it)
                Result(userError, null)
            }
    /**
     * класс ответа метода
     */
    class Result(val userError: UserError, private val _measurementsTask: MeasurementsTask?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
        val measurementsTask: MeasurementsTask
            get() = _measurementsTask!!
    }
}