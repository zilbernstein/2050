package ru.digipeople.locotech.inspector.ui.fragment.intercator

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс выполняющий методы принятия/отклонения на экране цикловые работы, замечания ОТК, замечания РЖД
 *
 * @author Kashonkov Nikita
 */
class WorkWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * принятьь все ОТК
     */
    fun acceptAllSld(workIds: List<String>) = mapToResult(thingsWorxWorker.acceptSLD(workIds))
    /**
     * принять все РЖД
     */
    fun acceptAllRzd(workIds: List<String>) = mapToResult(thingsWorxWorker.acceptRZD(workIds))
    /**
     * отклонить ОТК
     */
    fun declineSld(workId: String) = mapToResult(thingsWorxWorker.declineSLD(workId))
    /**
     * отклонить ржд
     */
    fun declineRzd(workId: String) = mapToResult(thingsWorxWorker.declineRZD(workId))

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