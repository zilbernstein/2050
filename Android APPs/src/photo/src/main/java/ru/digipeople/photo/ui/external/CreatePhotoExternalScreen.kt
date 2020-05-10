package ru.digipeople.photo.ui.external

import android.app.Activity
import android.content.Intent
import android.net.Uri
import ru.digipeople.photo.ui.activity.ActivityNavigator

/**
 * Абстракция для камеры
 *
 * @author Kashonkov Nikita
 */
class CreatePhotoExternalScreen {
    fun navigate(activity: Activity, activityNavigator: ActivityNavigator, requestCode: Int, uri: Uri) {
        activityNavigator.navigateToCreatePhoto(activity, requestCode, uri)
    }

    companion object {
        fun getResultFromIntent(resultCode: Int, intent: Intent?): Boolean {
            return if (resultCode == Activity.RESULT_OK) {
                return true
            } else {
                return false
            }
        }

    }


}