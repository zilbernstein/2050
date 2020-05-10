package ru.digipeople.locotech.inspector.ui.activity.equipment.adapter

import ru.digipeople.locotech.inspector.model.Equipment
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.DividerItem
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.EquipmentItem
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.item.SectionItem

/**
 * Вспомогательный класс для адаптера локомотивов
 */
class AdapterData : ArrayList<Any>() {
    /**
     * Проверка, что элемент - секция
     */
    fun isSection(position: Int): Boolean {
        val item = get(position)
        return item is SectionItem
    }
    /**
     * Получить секцию
     */
    fun getSection(position: Int): SectionItem {
        return get(position) as SectionItem
    }
    /**
     * Проверка, что элемент - оборудование
     */
    fun isEquipmentData(position: Int): Boolean {
        val item = get(position)
        return item is EquipmentItem
    }
    /**
     * Получить оборудование
     */
    fun getEquipment(position: Int): EquipmentItem {
        return get(position) as EquipmentItem
    }
    /**
     * Проверка, что элемент - линейное оборудование
     */
    fun isLineEquipmentData(position: Int): Boolean {
        val item = get(position)
        return item is Equipment
    }
    /**
     * Получение линейного оборудования
     */
    fun getLineEquipment(position: Int): Equipment {
        return get(position) as Equipment
    }
    /**
     * Проверка, что элемент - разделитель
     */
    fun isDividerView(position: Int): Boolean {
        val item = get(position)
        return item is DividerItem
    }
}