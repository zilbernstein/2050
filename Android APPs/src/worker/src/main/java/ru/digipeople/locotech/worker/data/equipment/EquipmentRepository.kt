package ru.digipeople.locotech.worker.data.equipment

import android.util.ArrayMap

/**
 * Репозиторий оборудования
 *
 * @author Kashonkov Nikita
 */
class EquipmentRepository private constructor() {
    /**
     * Лист оборудования
     */
    val list: MutableList<Equipment>
    /**
     * Map Оборудования
     */
    val map: ArrayMap<String, Equipment>

    init {
        val firstEquipment = Equipment("3С5К 13523450 0365А", 75)
        val secondEquipment = Equipment("3С5К 13523450 0365В", 50)

        list = mutableListOf(firstEquipment, secondEquipment)

        map = ArrayMap()
        map.put(firstEquipment.id, firstEquipment)
        map.put(secondEquipment.id, secondEquipment)
    }

    companion object {
        val INSTANCE = EquipmentRepository()
    }
}