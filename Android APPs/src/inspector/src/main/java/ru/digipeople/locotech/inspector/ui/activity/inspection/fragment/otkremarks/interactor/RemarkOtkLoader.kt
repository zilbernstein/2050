package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.response.RemarkSldResponse
import ru.digipeople.locotech.inspector.model.RemarkSldInfo
import ru.digipeople.locotech.inspector.model.mapper.remarkSldMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик замечаний ОТК
 *
 * @author Kashonkov Nikita
 */
class RemarkOtkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * получение замечаний
     */
    fun getRemarks(): Single<Result> {
        return thingsWorxWorker.getOTKRemarks()
//                .map { deleteEmptyRemarks(it) } // пока закомментировал, может еще понадобится
                .map { response ->
                    /**
                     * обработка результата
                     */
                    Result(UserError.NO_ERROR, remarkSldMapper.entityToModel(response))
                }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * удаление пустых замечаний
     */
    fun deleteEmptyRemarks(response: RemarkSldResponse): RemarkSldResponse {
        response.remarkList = response.remarkList.filter {
            it.workCount != 0
        }
        return response
    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError, val info: RemarkSldInfo?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}