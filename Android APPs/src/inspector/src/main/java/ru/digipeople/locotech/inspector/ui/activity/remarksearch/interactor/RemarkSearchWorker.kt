package ru.digipeople.locotech.inspector.ui.activity.remarksearch.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.response.CreateRemarkResponse
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий методы добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
class RemarkSearchWorker @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * добавить замечание
     */
    fun addRemark(remarkId: String): Single<Result> {
        return mapToResult(thingWorxWorker.addRemark(remarkId))
    }
    /**
     * создать замечания
     */
    fun createRemark(remarkName: String): Single<Result> {
        return mapToResult(thingWorxWorker.createRemark(remarkName))
    }

    private fun mapToResult(response: Single<CreateRemarkResponse>): Single<Result> {
        return response.map {
            Result(UserError.NO_ERROR, it.remarkId)
        }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }

    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError, val remarkId: String?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}