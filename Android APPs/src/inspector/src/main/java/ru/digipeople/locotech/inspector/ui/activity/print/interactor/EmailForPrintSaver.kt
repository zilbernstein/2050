package ru.digipeople.locotech.inspector.ui.activity.print.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Сохранение электронной почты
 */
class EmailForPrintSaver @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * Сохранение почты пользователя при отправке на печать
     */
    fun save(email: String): Single<UserError> {
        return thingsWorxWorker.setEmailForPrint(email)
                .map { UserError.NO_ERROR }
                .onErrorReturn { throwable ->
                    errorBuilder.fromThowable(throwable)
                }
    }
}