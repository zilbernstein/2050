package ru.digipeople.locotech.worker.data.equipment

/**
 * Модель оборудования, пока не готов сервер
 *
 * @author Kashonkov Nikita
 */
@Deprecated("demo")
data class Equipment(
    /**
     * ID оборудования
     */
    val id: String,
    /**
     * текущий прогресс
     */
    var progress: Int
)