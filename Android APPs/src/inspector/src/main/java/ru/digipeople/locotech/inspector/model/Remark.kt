package ru.digipeople.locotech.inspector.model

import ru.digipeople.locotech.core.data.model.Comment
import java.util.*

/**
 * Модель замечания
 *
 * @author Kashonkov Nikita
 */
class Remark {
    /**
     * Id
     */
    var id = ""

    /**
     * Название
     */
    var title = ""

    /**
     * Автор замечания
     */
    var author = ""

    /**
     * Дата создания замечания
     */
    lateinit var date: Date

    /**
     * Список работ
     */
    lateinit var works: List<Work>

    /**
     * Количество фотографий
     */
    var photoAmount = 0

    /**
     * Комментарий к замечанию
     */
    lateinit var comment: Comment

    /**
     * Количество работ в замечании
     */
    var workCount = 0
}