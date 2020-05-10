package ru.digipeople.locotech.master.ui.activity.worklist.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.mapper.WorkMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик добавления замечания / работ
 *
 * @author Kashonkov Nikita
 */
class WorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = WorkMapper.INSTANCE
    /**
     * Загрузка работ
     */
    fun loadWork(remarkId: String): Single<Result> {
        return thingsWorxWorker.getWorksFromRemark(remarkId)
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val works: List<Work>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}