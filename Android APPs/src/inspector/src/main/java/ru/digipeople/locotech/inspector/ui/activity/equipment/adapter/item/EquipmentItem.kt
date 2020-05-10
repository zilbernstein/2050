package ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item

import ru.digipeople.locotech.inspector.model.Equipment
/**
 * Модель локомотивов
 */
class EquipmentItem(val equipment: Equipment) {
    /**
     * Флаг того, что развернут список секций
     */
    var isExpanded = false
    /**
     * Список секций
     */
    lateinit var sections: List<SectionItem>
}