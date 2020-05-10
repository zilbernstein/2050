package ru.digipeople.message.api

import io.reactivex.Single
import ru.digipeople.message.api.model.response.ContactResponse
import ru.digipeople.message.api.model.response.MessageResponse
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.thingworx.model.IDListEntity
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Класс [DefaultThingsWorxWorker] - реализация интерфейса [ThingsWorxWorker]
 *
 * @author Kashonkov Nikita
 */
class DefaultThingsWorxWorker(private val thingWorx: ThingWorx,
                                     private val responseConverter: ResponseConverter) : ThingsWorxWorker {
    /**
     * Метод получения списка контактов
     */
    override fun getContacts(): Single<ContactResponse> {
        return thingWorx.getUsers()
                .map{ response->
                   responseConverter.convertToEntityList(response.result, ContactResponse::class.java)
                }
    }
    /**
     * Метод для получения списка сообщений
     */
    override fun getMessages(): Single<MessageResponse> {
        return thingWorx.getMessages()
                .map{ response->
                    responseConverter.convertToEntityList(response.result, MessageResponse::class.java)
                }
    }
    /**
     * Метод для отметки сообщения как прочитанного
     */
    override fun markMessageAsRread(id: String): Single<ResultResponse> {
        return thingWorx.markMessageAsRead(id)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для отправки сообщения
     */
    override fun sendMessage(contacts: Collection<String>, message: String): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(contacts)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.sendMessage(it, message) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
}