package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.EquipmentType
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
import java.util.*

/**
 * Модель оборудования
 *
 * @author Kashonkov Nikita
 */
class Equipment {
    /**
     * Id оборудования
     */
    var id: String = ""

    /**
     * Номер оборудования
     */
    var number: String = ""

    /**
     * Номер оборудования
     */
    var subNumber: String = ""

    /**
     * Бортовой номер
     */
    var name: String = ""

    /**
     * Тип оборудования
     */
    var type: EquipmentType = EquipmentType.UNDEFINED

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
    var sectionList: List<Section> = mutableListOf()

    /**
     * Дополнительно поле для получения количетсва всех работ по вкладкам
     */
    var worksCount: Map<WorksCounterViewType, Int> = emptyMap()

    override fun equals(other: Any?): Boolean {
        if (other !is Equipment) {
            return false
        }

        return this.id == other.id
    }
}
