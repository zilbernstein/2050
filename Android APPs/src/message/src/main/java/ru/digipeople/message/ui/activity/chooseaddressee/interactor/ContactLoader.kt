package ru.digipeople.message.ui.activity.chooseaddressee.interactor

import io.reactivex.Single
import ru.digipeople.message.api.ThingsWorxWorker
import ru.digipeople.message.model.Contact
import ru.digipeople.message.model.mapper.contactMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик контактов
 */
class ContactLoader @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * загрузка контактов
     */
    fun loadContacts(): Single<Result> {
        return thingWorxWorker.getContacts()
                .map { contactMapper.fromEntityList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    /**
                     * Обрпаботка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val contacts: List<Contact>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}