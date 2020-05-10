package ru.digipeople.locotech.master.ui.activity.remark.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий методы над замечаниями
 *
 * @author Kashonkov Nikita
 */
class RemarkWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * отмена работы
     */
    fun revokeWork(workId: String): Single<Result>{
        return mapToResult(thingsWorxWorker.deleteWork(workId))
    }
    /**
     * отмена замечания
     */
    fun revokeRemark(remarkId: String): Single<Result>{
        return mapToResult(thingsWorxWorker.deleteRemark(remarkId))
    }
    /**
     * преобразования данных к требуемому виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}