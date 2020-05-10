package ru.digipeople.locotech.worker.data.task

/**
 * Статус задания
 *
 * @author Kashonkov Nikita
 */
enum class TaskStatus(val description: String) {
    /**
     * Новая задача
     */
    NEW("Новая работа"),
    /**
     * В работе
     */
    IN_WORK("В работе"),
    /**
     * Приостановлена
     */
    PAUSED("Приостановлена"),
    /**
     * Отказ от работы
     */
    DECLINED("Отменена");

    companion object {
        fun valueOf(description: String) = values().firstOrNull { it.description.equals(description, true) }
    }
}