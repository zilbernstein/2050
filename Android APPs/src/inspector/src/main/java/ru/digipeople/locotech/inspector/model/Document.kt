package ru.digipeople.locotech.inspector.model

/**
 * Модель документа
 *
 * @author Kashonkov Nikita
 */
class Document{
    /**
     * Id документа
     */
    var id: String = ""
    /**
     * Название документа
     */
    lateinit var name: String
    /**
     * url документа
     */
    lateinit var url: String
}