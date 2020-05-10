package ru.digipeople.locotech.master.ui.activity.writeofftmcamount.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.mapper.InteractionResultMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                    private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = InteractionResultMapper.INSTANCE
    /**
     * Установить кол-во тмц для списания
     */
    fun setTmcAmount(idWork: String, idTMC: String, amount: Double): Single<Result> {
        return thingsWorxWorker.editTMCForWriteOff(idWork, idTMC, amount)
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