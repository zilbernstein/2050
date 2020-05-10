package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.mapper.InteractionResultMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
class AcceptWorkJob @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Созданиек маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
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
     * Стандартные ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}