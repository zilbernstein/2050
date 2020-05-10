package ru.digipeople.locotech.master.data.work

/**
 * Статусы работы
 *
 * @author Kashonkov Nikita
 */
enum class WorkStatus(val description: String) {
    /**
     * Новая
     */
    NEW("Новая"),
    /**
     * В работе
     */
    IN_WORK("В работе"),
    /**
     * Приосталновлена
     */
    PAUSED("Приостановленная"),
    /**
     * Задеражнная
     */
    EXPIRED("Просроченная"),
    /**
     * Успешна завершена
     */
    FINISHED("Выполненная"),
    /**
     * Принятая
     */
    ACCEPTED("Принята"),
    /**
     * Отменена
     */
    DELETED ("Отменено");


    companion object {
        fun valueOf(description: String) = values().firstOrNull { it.description.equals(description, true) }
    }
}