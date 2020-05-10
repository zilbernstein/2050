package ru.digipeople.locotech.worker.data.section

import ru.digipeople.locotech.worker.data.task.Task

/**
 * Модель секции, пока не готов сервер
 *
 * @author Kashonkov Nikita
 */
@Deprecated("demo")
class Section(
        /**
         * id секции
         */
        val id: String,
        /**
         * список заданий в секции
         */
        var tasks: MutableList<Task>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Section) return false
        if (this.id == other.id) {
            return true
        } else {
            return false
        }
    }
}