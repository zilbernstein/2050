package ru.digipeople.locotech.master.ui.activity.equipment.adapter

import ru.digipeople.locotech.master.model.Equipment
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.DividerItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.EquipmentItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.LineEquipmentItem
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.item.SectionItem

/**
 * Вспомогательный класс адаптера локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
class AdapterData() : ArrayList<Any>() {
    /**
     * определение что это секция
     */
    fun isSectionData(position: Int): Boolean {
        val item = get(position)
        return item is SectionItem
    }
    /**
     * получение данных секции
     */
    fun getSectionData(position: Int): SectionItem {
        return get(position) as SectionItem
    }
    /**
     * определение что это оборудование
     */
    fun isEquipmentData(position: Int): Boolean {
        val item = get(position)
        return item is EquipmentItem
    }
    /**
     * получение оборудования
     */
    fun getEquipment(position: Int): EquipmentItem {
        return get(position) as EquipmentItem
    }
    /**
     * определение что это линейное оборудование
     */
    fun isLineEquipmentData(position: Int): Boolean {
        val item = get(position)
        return item is LineEquipmentItem
    }
    /**
     * получение линейного оборудования
     */
    fun getLineEquipment(position: Int): LineEquipmentItem {
        return get(position) as LineEquipmentItem
    }
    /**
     * определение что это разделитель
     */
    fun isDividerView(position: Int): Boolean {
        val item = get(position)
        return item is DividerItem
    }
    /**
     * получение разделителя
     */
    fun getDividerView(position: Int): DividerItem {
        return get(position) as DividerItem
    }
    /**
     * получение данных итема по ID
     */
    fun getEquipmentDataViewById(equipment: Equipment): EquipmentItem? {
        var item: EquipmentItem? = null
        forEach {
            if (it is EquipmentItem) {
                if (it.equipment == equipment) {
                    item = it
                    return@forEach
                }
            }
        }
        return item
    }
}