package ru.digipeople.locotech.inspector.ui.activity.checklist.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс выполняющий методы по установке/снятию галочек(принять/отклонить) чек-листа
 *
 * @author Kashonkov Nikita
 */
class CheckListWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * принять сотрудником отк
     */
    fun acceptSld(workId: String) = mapToResult(thingsWorxWorker.acceptCsoSLD(workId))
    /**
     * отклонить сотрудником отк
     */
    fun declineSld(workId: String) = mapToResult(thingsWorxWorker.declineCsoSLD(workId))
    /**
     * принять сотрудником ржд
     */
    fun acceptRzd(workId: String) = mapToResult(thingsWorxWorker.acceptCsoRZD(workId))
    /**
     * отклонить сотрудником ржд
     */
    fun declineRzd(workId: String) = mapToResult(thingsWorxWorker.declineCsoRZD(workId))
    /**
     * преобразование данных к требуемому формату
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * стандартный ответ методов
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}