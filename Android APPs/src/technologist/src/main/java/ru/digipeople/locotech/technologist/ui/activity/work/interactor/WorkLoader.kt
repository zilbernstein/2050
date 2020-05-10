package ru.digipeople.locotech.technologist.ui.activity.work.interactor

import io.reactivex.Single
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.locotech.technologist.mapper.WorkDetailMapper
import ru.digipeople.locotech.technologist.model.Parameter
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик деталей работы
 */
class WorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    private val mapper = WorkDetailMapper.INSTANCE
    /**
     * Загрузка данных
     */
    fun loadWorkDetails(workId: String): Single<Result> {
        return thingsWorxWorker.getWorkDetail(workId)
                .map { mapper.entityToModel(it)!! }
                .map { Result(UserError.NO_ERROR, it.comment?.text ?: "", it.parametersList) }
                .onErrorReturn { error ->
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(error)
                    Result(userError, "", emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val comment: String, val parameters: List<Parameter>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR

    }
}