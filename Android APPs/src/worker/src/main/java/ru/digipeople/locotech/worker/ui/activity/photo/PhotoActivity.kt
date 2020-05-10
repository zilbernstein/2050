package ru.digipeople.locotech.worker.ui.activity.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.WindowManager
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryActivity
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryStartMode
import ru.digipeople.locotech.worker.R

/**
 * Активити для работы с фотографиями
 *
 * @author Kashonkov Nikita
 */
class PhotoActivity : PhotoGalleryActivity() {
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.appGray)
        }
    }
    override fun navigate() {
        /**
         * Полученеи данных из интента
         */
        val calledForResult = intent.getBooleanExtra(EXTRA_CALLING_FOR_RESULT, false)
        if(calledForResult){
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            navigateBack()
        }
    }

    companion object {
        //region Extra
        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_CALLING_FOR_RESULT = "EXTRA_CALLING_FOR_RESULT"
        private const val EXTRA_CALLING_TYPE = "EXTRA_CALLING_ID"
        //endRegion
        /**
         * Интент обычный
         */
        fun getCallingIntent(context: Context, remarkId: String): Intent {
            val intent = Intent(context, PhotoActivity::class.java)
            intent.putExtra(EXTRA_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.PHOTO_MODE_WORK)
            return intent
        }
        /**
         * Интент для возвращения результата
         */
        fun getCallingIntentForResult(context: Context, remarkId: String): Intent {
            val intent = Intent(context, PhotoActivity::class.java)
            intent.putExtra(EXTRA_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_FOR_RESULT, true)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.PHOTO_MODE_WORK)
            return intent
        }

        fun getExtra(responseCode: Int): Boolean{
            return responseCode == Activity.RESULT_OK
        }
    }
}