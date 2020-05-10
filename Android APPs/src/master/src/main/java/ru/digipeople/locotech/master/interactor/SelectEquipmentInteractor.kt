package ru.digipeople.locotech.master.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import ru.digipeople.locotech.core.data.api.mapper.ShortEquipmentMapper
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс выполняет метод выбора секции
 */
class SelectEquipmentInteractor @Inject constructor(private val thingsWorxWorker: CoreThingsWorxWorker,
                                                    private val errorBuilder: SimpleApiUserErrorBuilder,
                                                    private val sessionManager: SessionManager) {
    /**
     * функция выбора оборудования (секции)
     */
    suspend fun selectEquipment(equipmentId: String) = withContext(Dispatchers.IO) {
        try {
            val response = thingsWorxWorker.selectSection(equipmentId)
            val equipments = ShortEquipmentMapper.get().fromSelectSectionEquipments(response.entityList)
            /**
             * обновление выбранной секции
             */
            sessionManager.updateSelectedEquipment(equipmentId, equipments)
            Result(UserError.NO_ERROR)
        } catch (e: Exception) {
            val userError = errorBuilder.fromThowable(e)
            Result(userError)
        }
    }
    /**
     * Стандартный класс ответа метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}