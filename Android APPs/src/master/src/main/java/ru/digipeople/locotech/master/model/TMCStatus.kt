package ru.digipeople.locotech.master.model

/**
 * Модель статус ТМЦ
 *
 * @author Kashonkov Nikita
 */
enum class TMCStatus(val description: String) {
    /**
     *  Заказано
     */
    ORDERED("ORDERED"),
    /**
     * Собрано
     */
    COLLECTED("COLLECTED"),
    /**
     * Отпущено
     */
    ISSUED("ISSUED"),
    /**
     * Отказано
     */
    DENIED("DENIED"),

    /**
     * Неизввестен
     */
    UNKNOWN("");

    companion object {
        fun getBy(description: String) = values().firstOrNull { it.description.equals(description, true) }
    }
}