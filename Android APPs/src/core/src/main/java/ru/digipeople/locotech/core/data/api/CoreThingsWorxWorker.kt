package ru.digipeople.locotech.core.data.api

import ru.digipeople.locotech.core.data.api.response.SelectSectionResponse
import ru.digipeople.locotech.core.data.api.response.SignInResponse
import ru.digipeople.thingworx.helper.ApiException

/**
 * Интерфейс для работы с платформой ThingWorx
 */
interface CoreThingsWorxWorker {
    /*
     * Создание подключения
     */
    suspend fun connect(username: String, password: String): Boolean
    /**
     * дисконнект
     */
    suspend fun disconnect()
    /**
     * подключение
     */
    @Throws(ApiException::class)
    suspend fun signIn(fcmToken: String): SignInResponse
    /**
     * выбор секции
     */
    @Throws(ApiException::class)
    suspend fun selectSection(sectionId: String): SelectSectionResponse
}