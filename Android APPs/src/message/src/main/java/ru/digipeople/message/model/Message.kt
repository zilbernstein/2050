package ru.digipeople.message.model

import java.util.*

/**
 * Модель сообщения
 *
 * @author Kashonkov Nikita
 */
class Message {
    /**
     * Id
     */
    lateinit var id: String
    /**
     * Статус
     */
    lateinit var messageStatus: MessageStatus
    /**
     * Дата
     */
    lateinit var date: Date
    /**
     * Флаг входящее/исходящее
     */
    var isIncoming = false
    /**
     * Сообщение
     */
    lateinit var text: String
    /**
     * Отправитель
     */
    lateinit var sender: Contact
    /**
     * Список получателей
     */
    lateinit var recipients: ArrayList<Contact>
}