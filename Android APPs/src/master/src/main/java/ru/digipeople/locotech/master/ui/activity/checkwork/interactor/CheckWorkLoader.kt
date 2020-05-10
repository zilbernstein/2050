package ru.digipeople.locotech.master.ui.activity.checkwork.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.response.WorkResponse
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.mapper.WorkMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
class CheckWorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = WorkMapper.INSTANCE
    /**
     * Получение работ для согласования
     */
    fun loadWorkForApprove(): Single<Result> {
        return mapToResult(thingsWorxWorker.getWorksReadyForApprove())
    }
    /**
     * Получение списка работ для старта
     */
    fun loadWorkForStart(): Single<Result> {
        return mapToResult(thingsWorxWorker.getWorksReadyForStart())
    }
    /**
     * Получение списка работ для принятия мастером
     */
    fun loadWorkForMasterAccept(): Single<Result> {
        return mapToResult(thingsWorxWorker.getWorksReadyForMasterAccept())
    }
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<WorkResponse>): Single<Result> {
        return response.map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val equipment: List<Work>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}