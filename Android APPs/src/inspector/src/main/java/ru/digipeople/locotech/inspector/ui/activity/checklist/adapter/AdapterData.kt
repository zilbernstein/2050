package ru.digipeople.locotech.inspector.ui.activity.checklist.adapter

/**
 * Вспомогательный класс для работы с адаптером чеклиста
 *
 * @author Kashonkov Nikita
 */
class AdapterData : ArrayList<Any>() {
    /**
     * Проверка что элемент - оборудование
     */
    fun isEquipmentCsoData(position: Int): Boolean {
        val item = get(position)
        return item is EquipmentCSOData
    }
    /**
     * получение оборудования
     */
    fun getEquipmentCsoData(position: Int): EquipmentCSOData {
        return get(position) as EquipmentCSOData
    }
    /**
     * Проверка, что элемент - операция
     */
    fun isOperation(position: Int): Boolean {
        val item = get(position)
        return item is OperationData
    }
    /**
     * получение операции
     */
    fun getOperationData(position: Int): OperationData {
        return get(position) as OperationData
    }

}