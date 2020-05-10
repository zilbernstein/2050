package ru.digipeople.locotech.technologist.model

/**
 * Модель секции
 *
 * @author Sostavkin Grisha
 */
class Section {
    /**
     * Название/номер секции
     */
    lateinit var id: String
    /**
     * Список замечаний
     */
    lateinit var remarkList: List<Remark>
}