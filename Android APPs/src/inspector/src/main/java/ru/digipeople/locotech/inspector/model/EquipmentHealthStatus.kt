package ru.digipeople.locotech.inspector.model

/**
 * Модель статуса состояния оборудования
 *
 * @author Kashonkov Nikita
 */
enum class EquipmentHealthStatus(val description: String) {
    /**
     * Статус - зеленый
     */
    GREEN("Зеленый"),
    /**
     * желтый
     */
    YELLOW("Желтый"),
    /**
     * красный
     */
    RED("Красный");

    companion object {
        fun getValue(description: String) = EquipmentHealthStatus.values().firstOrNull { it.description.equals(description, true)}
    }
}