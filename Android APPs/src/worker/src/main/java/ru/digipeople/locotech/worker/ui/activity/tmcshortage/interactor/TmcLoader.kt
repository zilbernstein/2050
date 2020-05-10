package ru.digipeople.locotech.worker.ui.activity.tmcshortage.interactor

import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.TMCInWork
import ru.digipeople.locotech.worker.model.mapper.TMCInWorkMapper
import javax.inject.Inject

/**
 * Загрзчик ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                    private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Созадние маппера
     */
    val mapper = TMCInWorkMapper.INSTANCE
    /**
     * Загрухка данных
     */
    fun loadTMC(workId: String): Single<Result> {
        return thingsWorxWorker.loadTMCByWork(workId)
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
    data class Result(val userError: UserError,
                      val tmc: List<TMCInWork>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}