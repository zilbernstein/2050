package ru.digipeople.locotech.core.helper

import ru.digipeople.locotech.core.BuildConfig

/**
 * Объект для хранения флаворов
 */
object Flavor {
    const val isDev = BuildConfig.FLAVOR == "dev"
}