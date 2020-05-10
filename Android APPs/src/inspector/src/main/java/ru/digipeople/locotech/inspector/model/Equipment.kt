package ru.digipeople.locotech.inspector.model

import ru.digipeople.locotech.core.data.model.EquipmentType
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
import java.util.*

/**
 * Модель оборудования
 */
class Equipment {
    /**
     * Id оборудования
     */
    lateinit var id: String

    /**
     * Номер оборудования
     */
    lateinit var number: String

    /**
     * Номер оборудования
     */
    lateinit var subNumber: String

    /**
     * Бортовой номер
     */
    lateinit var name: String

    /**
     * Тип оборудования
     */
    lateinit var type: EquipmentType

    /**
     * Время нормативное
     */
    lateinit var timeRequired: Date

    /**
     * Время прошедшее
     */
    lateinit var timeLeft: Date

    /**
     * Прогресс выполнения
     */
    var progress: Int = 0

    /**
     * Флаг выбранности оборудования
     */
    var isSelected: Boolean = false

    /**
     * Список секций
     */
    var sections: List<Section> = mutableListOf()

    /**
     * Дополнительно поле для получения количетсва всех работ по вкладкам
     */
    var worksCount: Map<WorksCounterViewType, Int> = emptyMap()
}