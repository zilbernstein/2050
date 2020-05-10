package ru.digipeople.locotech.master.ui.activity.searchperformer.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.PerformerItem
import ru.digipeople.locotech.master.model.mapper.PerformerItemMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 *Загрузчик выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class PerformerLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = PerformerItemMapper.INSTANCE
    /**
     * Загрузка исполнителей
     */
    fun loadPerformers(workId: String): Single<Result> {
        return thingsWorxWorker.getPerformersList(workId)
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
    data class Result(val userError: UserError, val performers: List<PerformerItem>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}