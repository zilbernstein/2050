package ru.digipeople.message.api

import io.reactivex.Single
import ru.digipeople.message.api.model.response.ContactResponse
import ru.digipeople.message.api.model.response.MessageResponse
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Интерфейс для работы с апи
 *
 * @author Kashonkov Nikita
 */
interface ThingsWorxWorker {
    /**
     * Получения списка сообщений
     *
     * @return Список сообщений
     */
    fun getMessages(): Single<MessageResponse>
    /**
     * Получение списка адрессатов
     *
     * @return Списка адресатов
     */
    fun getContacts(): Single<ContactResponse>

    /**
     * Пометить сообщеия как прочтеенное
     *
     * @param id - id сообщения
     */
    fun markMessageAsRread(id: String): Single<ResultResponse>

    /**
     * Отправить сообщение
     *
     */
    fun sendMessage(contacts: Collection<String>, message: String): Single<ResultResponse>


}