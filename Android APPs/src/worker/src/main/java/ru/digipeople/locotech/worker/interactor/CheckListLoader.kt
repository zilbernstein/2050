package ru.digipeople.locotech.worker.interactor

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.Checklist
import ru.digipeople.locotech.worker.model.mapper.checklistMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Реализация загрузки чеклиста
 *
 * @author Sostavkin Grisha
 */
class CheckListLoader @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * Загрузка чек-листа
     */
    fun loadChecklist(workId: String): Single<Result> {
        return thingsWorxWorker.getChecklist(workId)
                .map { Result(UserError.NO_ERROR, checklistMapper.fromEntity(it)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, private val _checklist: Checklist?) {
        val isSuccessful
            get() = userError === UserError.NO_ERROR
        val checklist
            get() = _checklist!!
    }
}