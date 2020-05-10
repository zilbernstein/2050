package ru.digipeople.locotech.master.ui.activity.performance.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.mapper.WorkMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик исполнения
 *
 * @author Kashonkov Nikita
 */
class WorksLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                      private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание лоадера
     */
    val mapper = WorkMapper.INSTANCE
    /**
     * загрузка списка работ
     */
    fun loadWorks(): Single<Result> {
        return thingsWorxWorker.getWorksByEquipment()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * стандартный ответ метода
     */
    class Result(val userError: UserError, val works: List<Work>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}