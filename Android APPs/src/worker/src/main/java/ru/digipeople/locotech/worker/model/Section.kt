package ru.digipeople.locotech.worker.model

/**
 * Модель секции
 *
 * @author Kashonkov Nikita
 */
class Section {
    /**
     * Id секции
     */
    lateinit var sectionId: String
    /**
     * Название секции
     */
    lateinit var sectionName: String
    /**
     * Список работ
     */
    var workList: List<Work> = emptyList()
}