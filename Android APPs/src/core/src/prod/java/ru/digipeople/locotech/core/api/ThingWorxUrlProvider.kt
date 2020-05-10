package ru.digipeople.locotech.core.api

import ru.digipeople.locotech.core.BuildConfig
import ru.digipeople.locotech.core.data.CorePrefs
import javax.inject.Inject

/**
 * @author Aleksandr Brazhkin
 */
class ThingWorxUrlProvider @Inject constructor(private val corePrefs: CorePrefs) {

    private val default = BuildConfig.PROD_API_BASE_URL

    var current: String
        get() = corePrefs.thingWorksUrl ?: default
        set(url) {
            corePrefs.thingWorksUrl = url
        }

    val all = listOf(
            "prod" to BuildConfig.PROD_API_BASE_URL
    )
}