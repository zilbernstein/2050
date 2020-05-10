package ru.digipeople.locotech.metrologist.helper.session.interactor

import ru.digipeople.thingworx.BaseThingWorx
import javax.inject.Inject

/**
 * @author Aleksandr Brazhkin
 */
class LogoutInteractor @Inject constructor(private val baseThingWorx: BaseThingWorx) {
    /**
     * Выход
     */
    fun logout() {
        baseThingWorx.disconnectBlocking()
    }

}