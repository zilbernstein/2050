package ru.digipeople.locotech.inspector.model

/**
 * Модель состояния оборудования
 *
 * @author Kashonkov Nikita
 */
class EquipmentHealth{
    /**
     * Статус
     */
    lateinit var status: EquipmentHealthStatus
    /**
     * Тип
     */
    lateinit var type: EquipmentHealthType
    /**
     * позиция
     */
    var position = 0
}