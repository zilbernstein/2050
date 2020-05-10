package ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks

import ru.digipeople.locotech.technologist.model.Equipment

/**
 * Модель оборудования
 */
class EquipmentModel {
    /**
     * Оборудование
     */
    lateinit var equipment: Equipment
    /**
     * Список замечаний
     */
    lateinit var remarks: List<RemarkModel>
    /**
     * Признак что элемент развернут
     */
    var isExpanded = false
}