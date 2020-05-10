package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.WorksCounterViewType

/**
 * Модель секции
 *
 * @author Kashonkov Nikita
 */
class Section {
    /**
     * Id секции
     */
    lateinit var id: String

    /**
     * Номер секции
     */
    lateinit var number: String

    /**
     * Подномер секции
     */
    lateinit var subNumber: String

    /**
     * Наименование секции
     */
    lateinit var name: String

    /**
     * Тип секции
     */
    lateinit var type: String

    /**
     * Флаг выбранности секции
     */
    var isSelected: Boolean = false

    /**
     * Статус состояния секции
     */
    lateinit var equipmentHealth: List<EquipmentHealth>

    /**
     * Тип ремонта
     */
    var repairType: RepairType? = null

    /**
     * Состояние оборудования
     */
    var state: SectionState = SectionState.UNDEFINED

    /**
     * Список с числом работ по вкладкам
     */
    var worksCount: Map<WorksCounterViewType, Int> = emptyMap()
}