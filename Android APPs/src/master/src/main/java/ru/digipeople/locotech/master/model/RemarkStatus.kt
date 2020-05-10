package ru.digipeople.locotech.master.model

/**
 * Статус замечания
 *
 * @author Kashonkov Nikita
 */
enum class RemarkStatus(val code: Int) {
    /**
     * Работники по работа в замечании не назначены
     * Работы в замечании не согласованы
     */
    NO_WORKERS(1),
    /**
     * Работы в замечании не согласованы
     */
    NOT_SEND_TO_TECHNOLOGIST(2),
    /**
     * Работы в замечании согласованы
     */
    APPROVED(3);

    companion object {
        fun valueOf(code: Int) = values().firstOrNull { it.code == code }
    }
}