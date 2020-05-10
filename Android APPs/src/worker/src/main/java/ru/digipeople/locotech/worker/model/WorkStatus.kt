package ru.digipeople.locotech.worker.model

/**
 * Статусы работы
 *
 * @author Kashonkov Nikita
 */
enum class WorkStatus(val code: Int) {
    /**
     * Не согласована
     */
    NOT_APPROVED(0),
    /**
     * На одобрении
     */
    IN_APPROVE(1),
    /**
     * Одобрено
     */
    APPROVED(2),
    /**
     * В задании работника
     */
    IN_TASK(3),
    /**
     * В работе
     */
    IN_WORK(4),
    /**
     * Приостановлена рабочим
     */
    PAUSED(5),
    /**
     * Приостановлена мастером
     */
    STOPPED(6),
    /**
     * Выполнена
     */
    DONE(7),
    /**
     * Отказ от работы
     */
    CANCELED(8),
    /**
     * Принято Мастером
     */
    ACCEPTED_MASTER(9),
    /**
     * Принято ОТК
     */
    ACCEPTED_SLD(10),
    /**
     * Принято сотрудником ржд
     */
    ACCEPTED_RZD(11),
    /**
     * Отклонено технологом
     */
    REVOKED_TECHNOLOGIST(12),
    /**
     * Отклонено мастером
     */
    REVOKED_MASTER(13);

    companion object {
        fun valueOf(code: Int) = values().firstOrNull { it.code == code }
    }
}