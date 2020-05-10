package ru.digipeople.locotech.technologist.ui.activity.remarks.interactor

import io.reactivex.Single
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.locotech.technologist.mapper.InteractionResultMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс для подтверждения/отклонения работ
 */
class ApproveWorker @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * подтвердить работу
     */
    fun approveWorks(remarkId: String, workIds: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.approveWorks(remarkId, workIds))
    }
    /**
     * отклонить работу
     */
    fun rejectWorks(remarkId: String, workIds: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.rejectWorks(remarkId, workIds))
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
     * Стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}