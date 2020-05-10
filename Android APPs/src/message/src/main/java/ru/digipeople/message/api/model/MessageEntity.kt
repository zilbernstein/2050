package ru.digipeople.message.api.model

import com.google.gson.annotations.SerializedName

/**
 * Сущность сообщения с сервера
 *
 * @author Kashonkov Nikita
 */
class MessageEntity {
    /**
     * Id
     */
    @SerializedName("id")
    lateinit var id: String
    /**
     * Статус
     */
    @SerializedName("message_status")
    lateinit var messageStatus: String
    /**
     * Дата
    */
    @SerializedName("date")
    var date: Long = 0L
    /**
     * Флаг входящее/исходящее
     */
    @SerializedName("isIncomming")
    var isIncoming = false
    /**
     * Текст
     */
    @SerializedName("message_text")
    lateinit var text: String
    /**
     * Отправитель
     */
    @SerializedName("sender")
    lateinit var sender: ContactEntity
    /**
     * Список получателей
     */
    @SerializedName("recipient_list")
    lateinit var recipients: ArrayList<ContactEntity>
}