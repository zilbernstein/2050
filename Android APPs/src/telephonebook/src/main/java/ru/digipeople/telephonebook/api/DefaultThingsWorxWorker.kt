package ru.digipeople.telephonebook.api

import io.reactivex.Single
import ru.digipeople.telephonebook.api.response.ContactListResponse
import ru.digipeople.thingworx.helper.ResponseConverter

/**
 * Класс [DefaultThingsWorxWorker] - реализация интерфейса [ThingsWorxWorker] модуля телефонный справочник
 *
 * @author Sostavkin Grisha
 */
class DefaultThingsWorxWorker(private val thingWorx: ThingWorx,
                              private val responseConverter: ResponseConverter) : ThingsWorxWorker {
    /**
     * Получает список абонентов
     */
    override fun getContactList(): Single<ContactListResponse> {
        return thingWorx.getContactList()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, ContactListResponse::class.java)
                }
    }
}