package ru.digipeople.locotech.core.data

import ru.digipeople.locotech.core.api.ThingWorxUrlProvider
import ru.digipeople.thingworx.ThingWorxConfig
import ru.digipeople.thingworx.ThingWorxConfigProvider

/**
 * Провайдер для конфига ThingWorx
 */
class CoreThingWorxConfigProvider constructor(
        private val library: String,
        private val thingWorxUrlProvider: ThingWorxUrlProvider
) : ThingWorxConfigProvider {
    override fun getConfig(): ThingWorxConfig {
        return ThingWorxConfig(thingWorxUrlProvider.current, library)
    }
}