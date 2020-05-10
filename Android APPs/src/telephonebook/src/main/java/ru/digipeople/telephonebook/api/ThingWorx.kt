package ru.digipeople.telephonebook.api

import io.reactivex.Single
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Api для модуля телефонный справочник
 *
 * @author Sostavkin Grisha
 */
@Singleton
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx) {

    /**
     * Получает список абонентов
     *
     * @return ApiResponse со списоком абонентов
     */
    fun getContactList(): Single<ApiResponse> {
        return requestThingWorx("phonebook", emptyMap())
    }

    /**
     * Метод для общения с сервером
     *
     * @return ApiResponse c данными
     */
    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }
}