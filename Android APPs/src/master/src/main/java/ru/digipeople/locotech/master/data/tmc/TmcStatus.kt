package ru.digipeople.locotech.master.data.tmc

/**
 * Класс для статусов заказа ТМЦ
 *
 * @author Kashonkov Nikita
 */
enum class TmcStatus(val code: Int) {
    /**
     * Заказано
     */
    ORDERED(0),
    /**
     * Получено
     */
    RECEIVED(1),
    /**
     * Собрано
     */
    COMPILED(2),
    /**
     * Отказано
     */
    REFUSED(3);

    companion object {
        fun valueOf(code: Int) = TmcStatus.values().firstOrNull { it.code == code }
    }
}