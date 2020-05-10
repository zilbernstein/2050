package ru.digipeople.locotech.master.data.work

/**
 * Важность работы
 *
 * @author Kashonkov Nikita
 */
enum class WorkUrgence(val code: Int) {
    /**
     * Нормальная
     */
    NORMAL (1),

    /**
     * Повышенная
     */
    HIGH(2),

    /**
     * Срочно
     */
    URGENT(3);


    companion object {
        fun valueOf(code: Int) = WorkUrgence.values().firstOrNull { it.code == code }
    }
}