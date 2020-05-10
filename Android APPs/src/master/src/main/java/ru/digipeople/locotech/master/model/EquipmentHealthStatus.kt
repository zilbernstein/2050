package ru.digipeople.locotech.master.model

/**
 * Модель статуса состояния оборудования
 *
 * @author Kashonkov Nikita
 */
enum class EquipmentHealthStatus(val description: String) {
    /**
     * Показатель в отличном состоянии
     */
    GREEN("Зеленый"),
    /**
     * Показатель в хорошем состоянии
     */
    YELLOW("Желтый"),
    /**
     * Показатель в неудовлетворительном состоянии
     */
    RED("Красный");

    companion object {
        fun getValue(description: String) = EquipmentHealthStatus.values().firstOrNull { it.description.equals(description, true)}
    }
}