package ru.digipeople.thingworx.helper

import ru.digipeople.thingworx.model.base.BaseResponse
import ru.digipeople.thingworx.model.response.ErrorResponse
import ru.digipeople.thingworx.model.response.ResultResponse
import javax.inject.Inject

/**
 * Конвертор для ответа сервера
 *
 * @author Kashonkov Nikita
 */
class ResponseConverter @Inject constructor() {
    /**
     * Метод для конвертации ответа с сервера, содержащего список данных
     *
     * @param json - ответ сервера
     * @param clazz - тип ответа
     * @return Response с набором данных
     */
    @Throws(ApiException::class)
    fun <E> convertToEntityList(json: String, clazz: Class<E>): E {
        val response = convertList(json, clazz)
        return response
    }

    /**
     * Метод для конвертации ответа с сервера, содержащий результат взаимодействия с платформой
     * В случае, если в json пришла ошибка пробрасывается [ApiException]
     *
     * @param json - ответ сервера
     * @return Response с флагом успешного ответа
     */
    @Throws(ApiException::class)
    fun convertResultEntity(json: String): ResultResponse {
        val response = convertResultResponse(json)
        return response
    }

    /**
     * Метод для конвертации ответа с сервера
     *
     * @param json - ответ сервера
     * @param clazz - тип ответа
     * @return Response с набором данных
     */
    @Throws(ApiException::class)
    fun <E> convertToEntity(json: String, clazz: Class<E>): E {
        val response = convert<E>(json, clazz)
        return response
    }

    /**
     * Метод для конвертации ответа с сервера, содержащего список данных
     * В случае, если в json пришла ошибка пробрасывается [ApiException]
     *
     * @param json - ответ сервера
     * @param clazz - тип ответа
     * @return Response с набором данных
     */
    @Throws(ApiException::class)
    fun <E> convertList(json: String, clazz: Class<E>): E {
        val resultResponse = JsonConverter.parse(json, ResultResponse::class.java)
        if (resultResponse.successful) {
            return JsonConverter.parse(json, clazz)
        } else {
            val errorResponse = JsonConverter.parse(json, ErrorResponse::class.java)
            val error = errorResponse.error
            throw ApiException(error.code, error.description)
        }
    }

    /**
     * Метод для конвертации ответа с сервера, содержащий результат взаимодействия с платформой
     * В случае, если в json пришла ошибка пробрасывается [ApiException]
     *
     * @param json - ответ сервера
     * @return Response с флагом успешного ответа
     */
    @Throws(ApiException::class)
    fun convertResultResponse(json: String): ResultResponse {
        val resultResponse = JsonConverter.parse(json, ResultResponse::class.java)
        if (resultResponse.successful) {
            return resultResponse
        } else {
            val errorResponse = JsonConverter.parse(json, ErrorResponse::class.java)
            val error = errorResponse.error
            throw ApiException(error.code, error.description)
        }
    }

    /**
     * Метод для конвертации ответа с сервера, содержащий результат взаимодействия с платформой
     * В случае, если в json пришла ошибка пробрасывается [ApiException]
     *
     * @param json - ответ сервера
     * @return Response с флагом успешного ответа
     */
    @Throws(ApiException::class)
    fun <E> convert(json: String, clazz: Class<E>): E {
        val resultResponse = JsonConverter.parse(json, ResultResponse::class.java)
        if (resultResponse.successful) {
            val rawResponse = JsonConverter.parse(json, BaseResponse::class.java)
            return JsonConverter.parse(rawResponse.response, clazz)
        } else {
            val errorResponse = JsonConverter.parse(json, ErrorResponse::class.java)
            val error = errorResponse.error
            throw ApiException(error.code, error.description)
        }
    }
}