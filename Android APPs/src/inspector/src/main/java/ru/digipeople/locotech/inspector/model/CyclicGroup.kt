package ru.digipeople.locotech.inspector.model

/**
 * Модел группы цикловых работ
 *
 * @author Kashonkov Nikita
 */
class CyclicGroup{
    /**
     * Id группы
     */
    lateinit var id: String
    /**
     * Название группы
     */
    lateinit var groupName: String
    /**
     * Список работ в группе
     */
    lateinit var works: List<Work>
}