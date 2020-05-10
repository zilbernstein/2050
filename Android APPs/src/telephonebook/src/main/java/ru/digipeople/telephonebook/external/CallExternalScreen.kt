package ru.digipeople.locotech.master.ui.external

import android.app.Activity
import ru.digipeople.telephonebook.ui.TelephoneBookNavigator

/**
 * Навигация на совершение звонка
 *
 * @author Sostavkin Grisha
 */
class CallExternalScreen {
    /**
     * Переход к звонку
     */
    fun navigateToCall(activity: Activity, uri: String) {
        val activityNavigator = TelephoneBookNavigator()
        val telephoneNumber = "tel:"+ uri
        activityNavigator.navigateToCall(activity, telephoneNumber)
    }
}