package ru.digipeople.locotech.inspector.model
/**
 * Состояние секции
 */
enum class SectionState(val value: String) {
    /**
     * не определено
     */
    UNDEFINED("undefined"),
    /**
     * В ожидании
     */
    WAITING("waiting"),
    /**
     * В работе
     */
    IN_SERVICE("in_service");

    companion object {
        fun of(value: String) = values().find { it.value == value } ?: UNDEFINED
    }
}