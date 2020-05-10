package ru.digipeople.locotech.master.model
    /*
     * Состояния секции
     */
enum class SectionState(val value: String) {
    /*
     * Секция в неопределенном состоянии
     */
    UNDEFINED("undefined"),
        /*
         * Секция в ожидании обслуживании
         */
    WAITING("waiting"),
        /*
         * Секция на обслуживании
         */
    IN_SERVICE("in_service");

    companion object {
        fun of(value: String) = values().find { it.value == value } ?: UNDEFINED
    }
}