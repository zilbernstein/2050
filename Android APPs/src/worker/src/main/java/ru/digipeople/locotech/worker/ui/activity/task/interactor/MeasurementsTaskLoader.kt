package ru.digipeople.locotech.worker.ui.activity.task.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.MeasurementsTask
import ru.digipeople.locotech.worker.model.mapper.measurementsTaskMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик задания на проведение аппаратных замеров
 */
class MeasurementsTaskLoader @Inject constructor(
        private val thingWorxWorker: ThingsWorxWorker,
        private val userErrorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * Отправка запроса на проведение аппаратного замера
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
                 * обработка ошибки
                 */
                val userError = userErrorBuilder.fromThowable(it)
                Result(userError, null)
            }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, private val _measurementsTask: MeasurementsTask?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
        val measurementsTask: MeasurementsTask
            get() = _measurementsTask!!
    }
}