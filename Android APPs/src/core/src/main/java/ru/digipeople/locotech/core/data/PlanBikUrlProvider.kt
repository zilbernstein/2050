package ru.digipeople.locotech.core.data

import android.net.Uri
import ru.digipeople.locotech.core.api.ThingWorxUrlProvider

/**
 * Провайдер для ссылки на План БИК
 *
 * @author Michael Solenov
 */
class PlanBikUrlProvider(private val urlProvider: ThingWorxUrlProvider, private val mashup: String) {
    fun get(): Uri {
        val urlBuilder = Uri.parse(urlProvider.current).buildUpon()
        urlBuilder.scheme("http")
        urlBuilder.encodedPath("Thingworx/Runtime/index.html#mashup=$mashup")
        return urlBuilder.build()
    }
}