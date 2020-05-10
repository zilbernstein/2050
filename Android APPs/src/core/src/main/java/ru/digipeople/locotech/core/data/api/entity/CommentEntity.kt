package ru.digipeople.locotech.core.data.api.entity

import com.google.gson.annotations.SerializedName

/**
 * Сущность комментария
 */
class CommentEntity {
    /**
     * Дата создания
     */
    @SerializedName("datetime")
    var dateTime: Long = 0L
    /**
     * Автор комментария
     */
    @SerializedName("author")
    var author: String = ""
    /**
     * Текст комментария
     */
    @SerializedName("text")
    var text: String = ""
}