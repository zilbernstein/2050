package ru.digipeople.locotech.worker.model

import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель работы
 *
 * @author Kashonkov Nikita
 */
class Work {
    /**
     * Id работы
     */
    lateinit var workId: String
    /**
     * Название работы
     */
    lateinit var workName: String
    /**
     * Статус работы
     */
    lateinit var workStatus: WorkStatus

    /**
     * Количество повторов
     */
    var repeats: Int = 0
    /**
     * Исполнители
     */
    var workers: List<Performer> = emptyList()

}
