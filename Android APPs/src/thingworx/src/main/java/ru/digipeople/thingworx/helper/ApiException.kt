package ru.digipeople.thingworx.helper

/**
 * Ошибка выпонения запроса
 *
 * @author Kashonkov Nikita
 */
class ApiException(
        /**
         * Код ошибки
         */
        val code: Int,
        /**
         * Сообщение ошибки
         */
        message: String
) : Exception(message)