package ru.digipeople.locotech.metrologist.data.model

enum class SectionState(val value: String) {
    /**
     * Неопределено
     */
    UNDEFINED("undefined"),
    /**
     * В ожидании
     */
    WAITING("waiting"),
    /**
     * На обслуживании
     */
    IN_SERVICE("in_service");

    companion object {
        fun of(value: String) = values().find { it.value == value } ?: UNDEFINED
    }
}