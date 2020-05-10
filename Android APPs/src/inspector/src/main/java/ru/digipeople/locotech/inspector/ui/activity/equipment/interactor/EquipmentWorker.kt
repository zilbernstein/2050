package ru.digipeople.locotech.inspector.ui.activity.equipment.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.mapper.InteractionResultMapper
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющие метод выбора локомотивов/секций
 */
class EquipmentWorker @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * Создание маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * Выбор секции
     */
    fun selectSection(equipmentId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.setSelectedSection(equipmentId))
    }

    /**
     * Выбор оборудования
     */
    fun selectEquipment(equipmentId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.setSelectedEquipment(equipmentId))

    }

    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }

    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}