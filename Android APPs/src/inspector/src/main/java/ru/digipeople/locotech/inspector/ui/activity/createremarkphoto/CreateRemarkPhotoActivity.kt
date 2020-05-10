package ru.digipeople.locotech.inspector.ui.activity.createremarkphoto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.ui.activity.ActivityNavigator
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentParams
import ru.digipeople.locotech.inspector.ui.activity.inspection.StartTab
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryActivity
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryStartMode
import ru.digipeople.ui.activity.base.ActivityModule
import javax.inject.Inject

/**
 * Активити создания нового замечания
 *
 * @author Kashonkov Nikita
 */
class CreateRemarkPhotoActivity : PhotoGalleryActivity() {
    //region Di
    private lateinit var screenComponent: CreateRemarkPhotoScreenComponent
    private lateinit var component: CreateRemarkPhotoComponent
    @Inject
    lateinit var navigator: ActivityNavigator
    //end region
    private lateinit var remarkId: String
    private var startTab: Int = 0
    // region Other
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        /**
         * Извлечение данных из интента
         */
        remarkId = intent.getStringExtra(EXTRA_REMARK_ID)
        startTab = intent.getIntExtra(EXTRA_START_TYPE, StartTab.CYCLIC_WORK.code)
    }
    /**
     * Переход к комментариям
     */
    override fun navigate() {
        navigator.navigateToRemarkCommentActivity(this, CommentParams(remarkId, "", ""), START_COMMENT_REQUEST, startTab)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    private fun getScreenComponent(): CreateRemarkPhotoScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().createRemarkPhotoScreenomponent()
        } else {
            return saved as CreateRemarkPhotoScreenComponent
        }
    }

    /**
     * Получение результата
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            START_COMMENT_REQUEST -> {
                val isSuccesfull = CommentActivity.getResultFromIntent(resultCode)
                if (isSuccesfull) {
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    navigateBack()
                }
            }
        }
    }

    companion object {
        //region Extra
        private const val EXTRA_REMARK_ID = "EXTRA_ID"
        private const val EXTRA_START_TYPE = "EXTRA_START_TYPE"
        private const val START_COMMENT_REQUEST = 100
        //endRegion

        fun getCallingIntent(context: Context, remarkId: String, startTab: Int): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, CreateRemarkPhotoActivity::class.java)
            intent.putExtra(EXTRA_START_TYPE, startTab)
            intent.putExtra(EXTRA_REMARK_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.PHOTO_MODE_REMARK)
            return intent
        }

        fun getResultFromIntent(resultCode: Int): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }
}