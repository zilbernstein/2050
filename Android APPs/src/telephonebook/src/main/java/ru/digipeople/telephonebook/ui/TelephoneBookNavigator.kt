package ru.digipeople.telephonebook.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import ru.digipeople.locotech.master.ui.external.CallExternalScreen
import ru.digipeople.telephonebook.ui.activity.telephone.TelephoneBookActivity
import ru.digipeople.telephonebook.ui.activity.telephonedirectory.TelephoneDirectoryActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Внешний навигатор
 *
 * @author Kashonkov Nikita
 */
@Singleton
class TelephoneBookNavigator @Inject constructor() {

    fun navigateToCallActivity(activity: Activity, telephoneNumber: String) {
        val callExternalScreen = CallExternalScreen()
        callExternalScreen.navigateToCall(activity, telephoneNumber)
    }
    /**
     * переход к звонку
     */
    fun navigateToCall(activity: Activity, uri: String) {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse(uri))

        val packageManager = activity.packageManager
        val activities = packageManager.queryIntentActivities(callIntent, 0)
        val isIntentSafe = activities.isNotEmpty()

        if (isIntentSafe) {
            activity.startActivity(callIntent)
        }
    }
    /**
     * переход к экрану телефонной книги
     */
    fun navigateToTelephoneBookActivity(activity: Activity, launchType: Int) {
        val intent = if (launchType == LANDSCAPE) {
            TelephoneBookActivity.getCallingIntent(activity)
        } else {
            TelephoneDirectoryActivity.getCallingIntent(activity)
        }
        activity.startActivity(intent)
    }

    companion object {
        /**
         * Код для запуска в landscape режиме
         */
        val LANDSCAPE = 0
        /**
         * Код для запуска в portrait режиме
         */
        val PORTRAIT = 1
    }
}