package ru.digipeople.thingworx

/**
 * Интерфейс для получения конфига
 */
interface ThingWorxConfigProvider {
    fun getConfig(): ThingWorxConfig
}