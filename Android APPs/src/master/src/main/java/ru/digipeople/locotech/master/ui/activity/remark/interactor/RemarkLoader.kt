package ru.digipeople.locotech.master.ui.activity.remark.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.Remark
import ru.digipeople.locotech.master.model.mapper.RemarkMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик замечаний
 *
 * @author Kashonkov Nikita
 */
class RemarkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание маппера
     */
    val mapper = RemarkMapper.INSTANCE
    /**
     * загрузка данных по замечаниям
     */
    fun loadRemark(): Single<Result> {
        return thingsWorxWorker.getRemarkByEquipment()
                .map { response ->
                    /**
                     * обработка результата
                     */
                    val modelList = mapper.entityListToModelList(response.remarkList)
                    Result(UserError.NO_ERROR, modelList, response.equipmentPercent.toInt(), response.equipmentTimeLeft, response.equipmentTimeRequired)
                }
                .onErrorReturn {
                    /**
                     * обрабокта ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null, 0, 0L, 0L)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val remarks: List<Remark>?, val equipmentProgress: Int, val leftTime: Long, var requiredTime: Long) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}