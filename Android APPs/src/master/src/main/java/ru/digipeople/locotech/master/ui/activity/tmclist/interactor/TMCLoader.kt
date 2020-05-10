package ru.digipeople.locotech.master.ui.activity.tmclist.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.TMCInWork
import ru.digipeople.locotech.master.model.mapper.TMCInWorkMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                    private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    private val mapper = TMCInWorkMapper.INSTANCE
    /**
     * Загрузка ТМЦ
     */
    fun loadTmc(idWork: String): Single<Result> {
        return thingsWorxWorker.getTMCInWork(idWork)
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
    data class Result(val userError: UserError, val tmc: List<TMCInWork>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}