package ru.digipeople.locotech.master.ui.activity.checkwork.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.mapper.InteractionResultMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс подтверждения выбранных работ
 *
 * @author Kashonkov Nikita
 */
class CheckWorkJob @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * Запуск работы
     */
    fun startWork(workId: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.startWork(workId))
    }
    /**
     * Согласование работы
     */
    fun approveWork(workId: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.approveWork(workId))
    }
    /**
     * Принятие работы
     */
    fun acceptWork(workId: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.acceptWorks(workId))
    }
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }

    }
    /**
     * Стандартный класс ответа метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}