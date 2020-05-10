package ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks

/**
 * Вспомогательный класс для работы с адаптером замечаний
 */
class AdapterData : ArrayList<Any>() {
    /**
     * Проверка что элемент - оборудование
     */
    fun isEquipment(position: Int): Boolean {
        val item = get(position)
        return item is EquipmentModel
    }
    /**
     * Получение оборудования
     */
    fun getEquipment(position: Int): EquipmentModel {
        return get(position) as EquipmentModel
    }
    /**
     * Проверка что элемент - замечание
     */
    fun isRemark(position: Int): Boolean {
        val item = get(position)
        return item is RemarkModel
    }
    /**
     * Получение замечания
     */
    fun getRemark(position: Int): RemarkModel {
        return get(position) as RemarkModel
    }

}