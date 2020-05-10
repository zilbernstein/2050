package ru.digipeople.locotech.inspector.ui.activity.controlpoint.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.ControlPointEntity
import ru.digipeople.locotech.inspector.ui.activity.controlpoint.adapter.ControlPoint
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик контрольных точек
 * @author Sostavkin Grisha
 */
class ControlPointsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                              private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка данных
     */
    fun loadControlPoints(workId: String): Single<Result> {
        return thingsWorxWorker.getControlPoint(workId)
                .map { Result(UserError.NO_ERROR, mapToModel(it.checkPointsList), it.workName) }
                .onErrorReturn {
                    /**
                     * Обрабокта ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList(), "")
                }
    }
    /**
     * Преобразование данных к модели
     */
    private fun mapToModel(controlPointEntity: List<ControlPointEntity>) = controlPointEntity.map {
        ControlPoint().apply {
            id = it.id
            mark = it.mark
            name = it.name
        }
    }

    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val declinedReasonItems: List<ControlPoint>, val workName: String) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}
