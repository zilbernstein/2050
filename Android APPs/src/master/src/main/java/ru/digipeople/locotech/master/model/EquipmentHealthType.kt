package ru.digipeople.locotech.master.model

/**
 * Модель типы состояния оборудования
 *
 * @author Kashonkov Nikita
 */
enum class EquipmentHealthType(val description: String) {
    /**
     * Cроки
     */
    TIMELIMITS("Сроки"),
    /**
     * Персонал
     */
    STAFF("Персонал (Ресурсы)"),
    /**
     * Материалы
     */
    GOODS("Материалы"),
    /**
     * Оборудование участка
     */
    EQUIPMENTS("Оборудование участка"),
    /**
     * Прочее
     */
    OTHER("Прочее");

    companion object {
        fun getValue(description: String) = EquipmentHealthType.values().firstOrNull { it.description.equals(description, true)}
    }

}