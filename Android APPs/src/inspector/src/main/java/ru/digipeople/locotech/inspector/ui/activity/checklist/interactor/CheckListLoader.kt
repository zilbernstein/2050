package ru.digipeople.locotech.inspector.ui.activity.checklist.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.EquipmentCso
import ru.digipeople.locotech.inspector.model.mapper.equipmentCSOMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик чек-листа
 *
 * @author Kashonkov Nikita
 */
class CheckListLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * получить данные чек-листа
     */
    fun getCyclicWorks(): Single<Result> {
        return thingsWorxWorker.getChecklist()
                .map { response ->
                    /**
                     * обработка результата
                     */
                    val entities = response.entityList.filter { it.csoList.isNotEmpty() }
                    Result(UserError.NO_ERROR, equipmentCSOMapper.entityListToModelList(entities))
                }
                .onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val data: List<EquipmentCso>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}