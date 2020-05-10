package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.CyclicGroupsInfo
import ru.digipeople.locotech.inspector.model.mapper.*
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик цикловых работ
 *
 * @author Kashonkov Nikita
 */
class CyclicWorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                           private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Получить цикловые работы
     */
    fun getCyclicWorks(): Single<Result>{
        return thingsWorxWorker.getCyclicWorks()
                .map { Result(UserError.NO_ERROR, cyclicMapper.entityToModel(it)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val data: CyclicGroupsInfo?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}