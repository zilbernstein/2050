package ru.digipeople.locotech.core.data.model

import java.util.*

/**
 * Модель комментария
 *
 * @author Kashonkov Nikita
 */
class Comment {
    /**
     * Дата создания
     */
    lateinit var dateTime: Date
    /**
     * Автор комментария
     */
    lateinit var author: String
    /**
     * Текст комментария
     */
    lateinit var text: String
}