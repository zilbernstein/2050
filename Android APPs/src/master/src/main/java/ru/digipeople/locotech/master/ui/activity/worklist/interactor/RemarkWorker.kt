package ru.digipeople.locotech.master.ui.activity.worklist.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий добавление замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkWorker @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Добавление замечания
     */
    fun addRemark(remarkId: String): Single<Result> {
        return mapToResult(thingWorxWorker.addRemark(remarkId))
    }
    /**
     * Преобразование данных к требуемому результату
     */
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
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}