package ru.digipeople.locotech.master.data.work

/**
 * Статусы согласования работы
 *
 * @author Kashonkov Nikita
 */
enum class WorkApprovedStatus(val code: Int) {
    /**
     * Согласовано
     */
    APPROVED (1),

    /**
     * Не согласовано
     */
    NOT_APPROVED(2),

    /**
     * В процессе согласования
     */
    IN_PROGRESS(3);


    companion object {
        fun valueOf(code: Int) = WorkUrgence.values().firstOrNull { it.code == code }
    }
}