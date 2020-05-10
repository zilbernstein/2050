package ru.digipeople.locotech.master.model

/**
 * Модель статуса оборудования
 *
 * @author Kashonkov Nikita
 */
class EquipmentHealth{
    /*
     * Статус оборудования
     */
    lateinit var status: EquipmentHealthStatus
    /*
     * Тип
     */
    lateinit var type: EquipmentHealthType
    /*
     * Номер позиции для вывода
     */
    var position = 0
}