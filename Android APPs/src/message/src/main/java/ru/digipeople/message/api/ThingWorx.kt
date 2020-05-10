package ru.digipeople.message.api

import io.reactivex.Single
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject

/**
 * Класс [ThingWorx] - Api модуля message
 *
 * @author Kashonkov Nikita
 */
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx) {
    /**
     * Метод получения списка контактов
     *
     * @return [ApiResponse] со списком контактов
     */
    fun getUsers(): Single<ApiResponse>{
        return requestThingWorx("users", emptyMap())
    }

    /**
     * Метод для получения списка сообщений
     */
    fun getMessages(): Single<ApiResponse>{
        return  requestThingWorx("messages", emptyMap())
    }

    /**
     * Метод для отправки сообщения
     *
     * @param contactId - списко адрессатов
     * @param message - текста сообщения
     */
    fun sendMessage(contactId: String, message: String): Single<ApiResponse>{
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_recipient", contactId)
            map.put("message_text", message)
            map
        }.flatMap { requestThingWorx("send_message", it) }
    }

    /**
     * Метод для отметки сообщения как прочитанного
     *
     * @param id- id Сообщения
     * @return результат взаимодействия
     */
    fun markMessageAsRead(id: String): Single<ApiResponse>{
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id", id)
            map
        }.flatMap { requestThingWorx("mark_message_as_readed", it) }
    }

    /**
     * Метод для общения с сервером
     *
     * @return [ApiResponse] c данными
     */
    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }
}