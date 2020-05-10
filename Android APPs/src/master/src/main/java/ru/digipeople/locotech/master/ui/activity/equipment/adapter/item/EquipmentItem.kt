package ru.digipeople.locotech.master.ui.activity.equipment.adapter.item

import ru.digipeople.locotech.master.model.Equipment
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.AdapterData

/**
 * Модель данных локомотивов для AdapterDate с флагом того что у него развернуто поле секций
 *
 * @author Kashonkov Nikita
 */
class EquipmentItem(val equipment: Equipment) {
    /**
     * Флаг того что развернут список секций
     */
    var isExpanded = false
    /**
     * Набор данных из поля SectionList сущности [Equipment]
     */
    lateinit var sectionData: AdapterData
}