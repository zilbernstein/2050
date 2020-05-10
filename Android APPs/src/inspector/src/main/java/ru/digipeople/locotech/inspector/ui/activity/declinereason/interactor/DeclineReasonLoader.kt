package ru.digipeople.locotech.inspector.ui.activity.declinereason.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonModel
import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * @author Sostavkin Grisha
 */
class DeclineReasonLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                              private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = DeclinedReasonMapper.INSTANCE
    /**
     * загрузка причин для удаления замечания
     */
    fun loadReasons(): Single<Result> {
        return thingsWorxWorker.getDeclinedReasons()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val declinedReasonItems: List<DeclinedReasonModel>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}