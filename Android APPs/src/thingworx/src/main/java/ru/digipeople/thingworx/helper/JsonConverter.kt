package ru.digipeople.thingworx.helper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

/**
 * Парсер ответов с ThingWorx, приходящих в виде
 *
 * @author Kashonkov Nikita
 */
class JsonConverter {

    companion object {

        /**
         * Парсит переданный JSON в объект переданнаго класса
         *
         * @param json - входящий JSON
         * @param clazz - класс в который происходит ковертация
         * @return сущности типа [T]
         */
        fun <T> parse(json: String, clazz: Class<T>): T {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder.create()

            val obj = gson.fromJson(json, clazz)
            return obj
        }

        /**
         * Парсит переданный JSON в объект переданнаго класса
         *
         * @param json - входящий JSON
         * @param clazz - класс в который происходит ковертация
         * @return сущности типа [T]
         */
        fun <T> parse(json: JsonObject, clazz: Class<T>): T {
            val gsonBuilder = GsonBuilder()
            val gson = gsonBuilder.create()

            val obj = gson.fromJson(json.toString(), clazz)
            return obj
        }

        /**
         * Преобразует переданный объект в JSON
         *
         * @param obj - объект для преобразования в JSON
         * @return JSON в представлении строки
         */
        fun convertToJson(obj: Any): String {
            val json = Gson().toJson(obj)
            return json
        }
    }

}