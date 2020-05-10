package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.WriteOffTmc
import ru.digipeople.locotech.master.model.mapper.WriteOffTmcMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
class TmcLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                    private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Созадние маппера
     */
    val mapper = WriteOffTmcMapper.INSTANCE
    /**
     * Загрузка тмц
     */
    fun loadTmc(workId: List<String>): Single<Result> {
        return thingsWorxWorker.getTmcForWriteOff(workId)
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    if (userError.message == errorBuilder.unknownError.message) {
                        // Хак по запросу Ковалевского, потому что некому сейчас починить сервер
                        return@onErrorReturn Result(UserError.NO_ERROR, emptyList())
                    }
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val data: List<WriteOffTmc>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}