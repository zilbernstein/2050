package ru.digipeople.locotech.inspector.model

import ru.digipeople.locotech.core.data.model.Comment

/**
 * Модель работы
 *
 * @author Kashonkov Nikita
 */
class Work {
    /**
     * Id работы
     */
    lateinit var id: String
    /**
     * Количество фотографий
     */
    var photosCount = 0
    /**
     * Название работы
     */
    lateinit var title: String
    /**
     * Флаг наличия контрольных точек
     */
    var hasCheckpoints = false
    /**
     * Статус
     */
    lateinit var status: WorkStatus
    /**
     * Комментарий
     */
    lateinit var comment: Comment
}