package ru.digipeople.thingworx.model.base

import com.google.gson.annotations.SerializedName

/**
 * Базовый класс для ответов, содержащих в себе коллекцию элементов
 *
 * @author Kashonkov Nikita
 */
open class BaseCollectionResponse<T> {
    @SerializedName("data")
    lateinit var entityList: ArrayList<T>
}