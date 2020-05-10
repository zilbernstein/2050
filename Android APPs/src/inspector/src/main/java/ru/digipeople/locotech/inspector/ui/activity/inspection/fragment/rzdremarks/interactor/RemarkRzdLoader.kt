package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.response.RemarkRzdResponse
import ru.digipeople.locotech.inspector.model.RemarkRzdInfo
import ru.digipeople.locotech.inspector.model.mapper.remarkRzdMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
class RemarkRzdLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * получить список замечаний
     */
    fun getRemarks(): Single<Result> {
        return thingsWorxWorker.getRzdRemarks()
//                .map { deleteEmptyRemarks(it) } // пока закомментировал, может еще понадобится
                .map { response ->
                    /**
                     * обработка результата
                     */
                    Result(UserError.NO_ERROR, remarkRzdMapper.entityToModel(response))
                }
                .onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }

    fun deleteEmptyRemarks(response: RemarkRzdResponse): RemarkRzdResponse {
        response.remarkList = response.remarkList.filter {
            it.workCount != 0
        }
        return response
    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError, val info: RemarkRzdInfo?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}