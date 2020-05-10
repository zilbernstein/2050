package ru.digipeople.locotech.master.ui.activity.ordertorepair.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.request.OrderToRepairRequest
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс запрос на получение ТМЦ для ремонта
 */
class OrderToRepairWorker @Inject constructor(
        private val thingWorxWorker: ThingsWorxWorker,
        private val userErrorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * отправка запроса на получение ТМЦ для ремонта
     */
    fun setOrderToRepair(sectionId: String): Single<Result> = thingWorxWorker.orderToRepair(OrderToRepairRequest(sectionId = sectionId))
            .map { response ->
                /**
                 * обработка результата
                 */
                Result(UserError.NO_ERROR, response.status)
            }
            .onErrorReturn { Result(userErrorBuilder.fromThowable(it), "") }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val status: String) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}