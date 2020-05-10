package ru.digipeople.telephonebook.interactor

import io.reactivex.Single
import ru.digipeople.telephonebook.api.ThingsWorxWorker
import ru.digipeople.telephonebook.mapper.ContactMapper
import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик контактов
 *
 * @author Sostavkin Grisha
 */
class ContactsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                         private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = ContactMapper.INSTANCE
    /**
     * Загрузка контактов
     */
    fun loadContact(): Single<Result> {
        return thingsWorxWorker.getContactList()
                .map { response ->
                    /**
                     * Обработка результата
                     */
                    val models = mapper.entityListToModelList(response.entityList)
                    models.filter { it.name.isNotEmpty() && it.phoneNumber.isNotEmpty() }
                }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn { error ->
                    val userError = errorBuilder.fromThowable(error)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val contactList: List<Contact>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}