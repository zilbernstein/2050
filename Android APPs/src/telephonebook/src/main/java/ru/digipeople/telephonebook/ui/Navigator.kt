package ru.digipeople.telephonebook.ui

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигатор для модуля телефонный справочник
 *
 * @author Sostavkin Grisha
 */
@Singleton
class Navigator @Inject constructor(private val activityNavigator: TelephoneBookNavigator) {
    //region Other
    private var activity: Activity? = null
    //end Region

    internal fun onResume(activity: Activity) {
        this.activity = activity
    }

    internal fun onPause() {
        this.activity = null
    }
    /**
     * переход к звонку
     */
    fun navigateToCall(uri: String) {
        if (activity != null) {
            activityNavigator.navigateToCallActivity(activity!!, uri)
        }
    }
    /**
     * переход к телефонной книге
     */
    fun navigateToTelephoneBookActivity(launchType: Int) {
        if (activity != null) {
            activityNavigator.navigateToTelephoneBookActivity(activity!!,launchType)
        }
    }
}