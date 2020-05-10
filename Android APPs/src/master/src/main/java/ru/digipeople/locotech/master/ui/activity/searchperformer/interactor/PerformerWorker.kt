package ru.digipeople.locotech.master.ui.activity.searchperformer.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.mapper.InteractionResultMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс добавляющий сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class PerformerWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder)  {
    /**
     * Создание мапппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * Добавление исполниетелей
     */
    fun addPerfromers(workId: String, performerIdList: List<String>): Single<Result> {
        return thingsWorxWorker.setPerformers(workId, performerIdList)
                .map {
                    Result(UserError.NO_ERROR)
                }
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