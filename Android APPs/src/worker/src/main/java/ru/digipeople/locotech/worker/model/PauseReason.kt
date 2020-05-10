package ru.digipeople.locotech.worker.model

/**
 * Модель причины остановки работы
 *
 * @author Kashonkov Nikita
 */
class PauseReason {
    /**
     * Id причины остановки
     */
    lateinit var id: String
    /**
     * Название причины остановки
     */
    lateinit var reasonName: String
}