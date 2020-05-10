package ru.digipeople.locotech.core.helper.session.interactor

import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import javax.inject.Inject

/**
 * Класс для выхода из системы платформы ThingWorx
 */
class LogoutInteractor @Inject constructor(private val thingsWorxWorker: CoreThingsWorxWorker) {
    suspend fun logout() {
        /**
         * дисконнект
         */
        thingsWorxWorker.disconnect()
    }
}