package ru.digipeople.locotech.worker.model

import ru.digipeople.locotech.core.data.model.Comment
import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель деталки работы
 *
 * @author Kashonkov Nikita
 */
class WorkDetail {
    /**
     * Название работы
     */
    lateinit var workName: String
    /**
     * Статус работы
     */
    lateinit var workStatus: WorkStatus
    /**
     * Время для выполнения работы
     */
    var timeLimit = 0L
    /**
     * Прошедшее время
     */
    var timeRemain = 0L
    /**
     * Название оборудования
     */
    lateinit var equipmentName: String
    /**
     * Номер оборудования
     */
    lateinit var equipmentNumber: String
    /**
     * Прогресс по оборудованию
     */
    var equipmentProgress: Int = 0
    /**
     * Список ТМЦ
     */
    var camList: List<TMCInWork> = emptyList()
    /**
     * Комментарий
     */
    var comment: Comment? = null

    /**
     * Количество повторов
     */
    var repeats: Int = 0
    /**
     * Исполнители
     */
    var workers: List<Performer> = emptyList()
    /**
     * Статус получения аппаратных замеров
     */
    var measurementStatus = MeasurementStatus.NO_TASK
}