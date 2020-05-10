package ru.digipeople.locotech.worker.data.reason

/**
 * Модель причины остановки работы, пока не готов сервер
 *
 * @author Kashonkov Nikita
 */
data class Reason(
        /**
         * Id причины
         */
        val id: Int,
        /**
         * Название причины
         */
        val title: String
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Reason) return false
        if (this.id == other.id) {
            return true
        } else {
            return false
        }
    }
}