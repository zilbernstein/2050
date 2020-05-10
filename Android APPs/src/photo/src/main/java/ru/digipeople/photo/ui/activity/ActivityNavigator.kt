package ru.digipeople.photo.ui.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import ru.digipeople.photo.R
import ru.digipeople.photo.ui.external.CreatePhotoExternalScreen
import ru.digipeople.ui.activity.base.BaseActivityNavigator
import java.io.File
import javax.inject.Inject

/**
 * Навигатор для активити
 *
 * @author Kashonkov Nikita
 */
class ActivityNavigator @Inject constructor() : BaseActivityNavigator() {
    fun navigateToCreatePhoto(activity: Activity, requestCode: Int, photoPath: Uri) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath)
        try {
            activity.startActivityForResult(takePictureIntent, requestCode)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(activity, R.string.create_photo_error_no_camera, android.widget.Toast.LENGTH_LONG).show()
        }

    }
    /**
     * Переход к созданию фото
     */
    fun navigateToCreatePhotoActivity(activity: Activity, requestCode: Int, uri: Uri) {
        val photoExternalScreen = CreatePhotoExternalScreen()
        photoExternalScreen.navigate(activity, this, requestCode, uri)
    }


}