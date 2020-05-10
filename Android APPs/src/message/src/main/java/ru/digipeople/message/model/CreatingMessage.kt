package ru.digipeople.message.model

/**
 * Модель создания сообщения
 *
 * @author Kashonkov Nikita
 */
class CreatingMessage {
    /**
     * Список контактов
     */
    var contacts: MutableMap<String, Contact> = mutableMapOf()
    /**
     * Текст сообщения
     */
    var text: String = ""
}