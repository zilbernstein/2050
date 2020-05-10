package ru.digipeople.locotech.core.data.api

import ru.digipeople.locotech.core.data.api.request.SelectSectionRequest
import ru.digipeople.locotech.core.data.api.request.SignInRequest
import ru.digipeople.locotech.core.data.api.response.SelectSectionResponse
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.helper.ApiException
import ru.digipeople.thingworx.helper.ResponseConverter
import javax.inject.Inject

/**
 * Реализация интерфейса [CoreThingsWorxWorker]
 */
class DefaultCoreThingsWorxWorker @Inject constructor(
        private val baseThingWorx: BaseThingWorx,
        private val responseConverter: ResponseConverter
) : CoreThingsWorxWorker {
    /**
     * соединение
     */
    override suspend fun connect(username: String, password: String): Boolean {
        return baseThingWorx.connect(username, password)
    }
    /**
     * дисконнект
     */
    override suspend fun disconnect() {
        baseThingWorx.disconnect()
    }
    /**
     * авторизация
     */
    @Throws(ApiException::class)
    override suspend fun signIn(fcmToken: String): SignInResponse {
        val request = SignInRequest(fcmToken)
        val rawResponse = baseThingWorx.requestThingWorx("signin_v2", request)
        return responseConverter.convertToEntity(rawResponse.result, SignInResponse::class.java)
    }
    /**
     * выбор секции
     */
    @Throws(ApiException::class)
    override suspend fun selectSection(sectionId: String): SelectSectionResponse {
        val request = SelectSectionRequest(sectionId)
        val rawResponse = baseThingWorx.requestThingWorx("select_section_v2", request)
        return responseConverter.convertToEntityList(rawResponse.result, SelectSectionResponse::class.java)
    }
}