package ru.digipeople.locotech.inspector.ui.activity.print.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.SignersCategoryEntity
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс, выполняющий метод отправку на печать документов с заданными подписантами на указанный адрес электронной почты
 *
 * @author Aleksandr Brazhkin
 */
class PrintWorker @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                      private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Отправить на печать
     */
    fun sendToPrint(email: String, documentsForPrint: List<String>, signerCategories: List<SignersCategoryEntity>): Single<Result> {
        return thingsWorxWorker.printProtocol(email, documentsForPrint, signerCategories)
                .map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}