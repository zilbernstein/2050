package ru.digipeople.telephonebook.api

import io.reactivex.Single
import ru.digipeople.telephonebook.api.response.ContactListResponse

/**
 * Интерфейс для рабоыт с Апи
 *
 * @author Sostavkin Grisha
 */
interface ThingsWorxWorker {

    /**
     * Получает список контактов
     */
    fun getContactList(): Single<ContactListResponse>
}