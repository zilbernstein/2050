package ru.digipeople.locotech.technologist.model

import ru.digipeople.locotech.core.data.model.Comment
import java.util.*

/**
 * Модель замечания
 *
 * @author Sostavkin Grisha
 */
class Remark {
    /**
     * Id замечания
     */
    lateinit var id: String
    /**
     * Название замечания
     */
    lateinit var name: String
    /**
     * Комментарий к замечанию
     */
    lateinit var comment: Comment
    /**
     * Дата замечания
     */
    lateinit var date: Date
    /**
     * Количество фотографий по замечанию
     */
    var photoAmount: Long = 0L
    /**
     * Работы по замечанию
     */
    lateinit var workList: List<Work>
    /**
     * Автор замечания
     */
    lateinit var author: String
}