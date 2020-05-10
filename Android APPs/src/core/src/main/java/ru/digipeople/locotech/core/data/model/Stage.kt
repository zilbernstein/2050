package ru.digipeople.locotech.core.data.model

/**
 * enum для этапа замера
 *
 * @author Sostavkin Grisha
 */
enum class Stage(var value: Int, var title: String) {
    /**
     * До ремонта
     */
    BEFORE_REPAIR(0, "До ремонта"),
    /**
     * Контрольный
     */
    CONTROL(1, "Контрольный"),
    /**
     * После ремонта
     */
    AFTER_REPAIR(2, "После ремонта");

    companion object {
        val titles
            get() = values().map { it.title }

        fun valueOf(value: Int) = values().firstOrNull { it.value == value } ?: throw IllegalArgumentException("Unable to find stage by value $value")

        fun of(value: String) = values().firstOrNull { it.title == value } ?: throw IllegalArgumentException("Unable to find stage by value $value")
    }
}