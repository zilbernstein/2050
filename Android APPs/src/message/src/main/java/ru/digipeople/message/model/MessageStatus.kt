package ru.digipeople.message.model

/**
 * enum для статуса сообщения 
 */
enum class MessageStatus {
    /**
     * Отправлено
     */
    SENT,
    /**
     * Прочитано
     */
    READ,
    /**
     * Статус неизвестен
     */
    UNKNOWN
}