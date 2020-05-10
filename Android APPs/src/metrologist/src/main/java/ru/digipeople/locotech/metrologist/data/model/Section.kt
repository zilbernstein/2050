package ru.digipeople.locotech.metrologist.data.model

/**
 * Модель секции
 */
class Section {
    /**
     * Id секции
     */
    var id = ""

    /**
     * Номер секции
     */
    var number = ""

    /**
     * Подномер секции
     */
    var subNumber = ""

    /**
     * Наименование секции
     */
    var name = ""

    /**
     * Флаг выбранности секции
     */
    var isSelected = false

    /**
     * Виды ремонта
     */
    var repairType: RepairType? = null

    /**
     * Статус
     */
    var state: SectionState = SectionState.UNDEFINED
}