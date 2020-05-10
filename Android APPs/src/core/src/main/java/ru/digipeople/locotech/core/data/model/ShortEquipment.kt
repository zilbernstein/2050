package ru.digipeople.locotech.core.data.model

/**
 * Сокращенная модель оборудования
 */
class ShortEquipment {
    /**
     * UUID выбранного оборудования (секции)
     */
    var id = ""
    /**
     * Наименование оборудования (секции)
     */
    var name = ""
    /**
     * Тип оборудования
     */
    lateinit var type: EquipmentType
}