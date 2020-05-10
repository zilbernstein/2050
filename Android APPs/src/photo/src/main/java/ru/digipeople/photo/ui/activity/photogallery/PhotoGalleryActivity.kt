package ru.digipeople.photo.ui.activity.photogallery

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import ru.digipeople.photo.PhotoComponent
import ru.digipeople.photo.R
import ru.digipeople.photo.helper.ImageLoader
import ru.digipeople.photo.model.Photo
import ru.digipeople.photo.ui.activity.ActivityNavigator
import ru.digipeople.photo.ui.activity.photogallery.adapter.PhotoAdapter
import ru.digipeople.photo.ui.activity.photogallery.decorator.PhotoDecorator
import ru.digipeople.photo.ui.activity.photogallery.dialog.ProgressDialog
import ru.digipeople.photo.ui.external.CreatePhotoExternalScreen
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.BaseRtPermissionsDelegate
import ru.digipeople.ui.delegate.CameraRtPermissionDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити фотогалереи
 *
 * @author Kashonkov Nikita
 */
open class PhotoGalleryActivity : MvpActivity(), PhotoGalleryView {
    //region DI
    private lateinit var screenComponent: PhotoGalleryScreenComponent
    private lateinit var component: PhotoGalleryComponent
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var adapter: PhotoAdapter
    @Inject
    lateinit var cameraRtPermissionsDelegate: CameraRtPermissionDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //regionView
    lateinit var parentLayout: ConstraintLayout
    lateinit var photoView: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var backImage: ImageView
    lateinit var checkImage: ImageView
    lateinit var createPhotoButton: ImageButton
    //endRegion
    //region Other
    private val TAKE_PHOTO_REQUEST_CODE = 101
    private lateinit var presenter: PhotoGalleryPresenter
    private lateinit var startMode: PhotoGalleryStartMode
    private var progressDialog: ProgressDialog? = null
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val startMode = intent.getSerializableExtra(EXTRA_CALLING_TYPE) as PhotoGalleryStartMode

        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .id(intent.getStringExtra(EXTRA_ID))
                .startMode(startMode)
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, PhotoGalleryPresenter::class.java)
        presenter.initialize()

        cameraRtPermissionsDelegate.setCallback(rtPermissionsDelegateCallback)
        cameraRtPermissionsDelegate.onCreate(savedInstanceState)

        parentLayout = findViewById(R.id.parent_layout)

        photoView = findViewById(R.id.activity_photo_gallery_current_photo)
        /**
         * Обработка нажатия на фото/удаление фото
         */
        adapter.onPhotoClickListener = presenter::onPhotoClicked
        adapter.onDeleteClickListener = presenter::onPhotoDeleteClicked

        createPhotoButton = findViewById(R.id.activity_photo_gallery_create_photo)
        createPhotoButton.setOnClickListener { presenter.onCreatePhotoClicked() }

        recyclerView = findViewById(R.id.activity_photo_recycler)
        /**
         * оформление в зависимости от ориентации
         */
        lateinit var layoutManager: LinearLayoutManager
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = GridLayoutManager(this, 2)
            val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.photo_transparent_divider)!!)
            recyclerView.addItemDecoration(dividerItemDecoration)

        } else {
            layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val photoDecorator = PhotoDecorator(this, 16f)
            recyclerView.addItemDecoration(photoDecorator)
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        backImage = findViewById(R.id.back_button)
        backImage.setOnClickListener { presenter.onBackClicked() }

        checkImage = findViewById(R.id.check_button)
        checkImage.setOnClickListener { presenter.onCheckClicked() }
    }

    override fun onResume() {
        super.onResume()
        cameraRtPermissionsDelegate.onResume()
    }

    override fun onDestroy() {
        cameraRtPermissionsDelegate.onDestroy()
        super.onDestroy()
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        presenter.destroy()
        super.onBackPressed()
    }
    /**
     * Результат запроса разрешения
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        cameraRtPermissionsDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        cameraRtPermissionsDelegate.onDestroy()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установить фото
     */
    override fun setPhoto(photo: Photo?) {
        if (photo == null) {
            photoView.visibility = View.GONE
        } else {
            photoView.visibility = View.VISIBLE
            ImageLoader(photoView, R.color.appTransparent, photo.url).load()
        }
    }
    /**
     * переход к созданию фото
     */
    override fun navigateToCreatePhoto(uri: Uri) {
        activityNavigator.navigateToCreatePhotoActivity(this@PhotoGalleryActivity, TAKE_PHOTO_REQUEST_CODE, uri)
    }
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Photo>) {
        val dataList = mutableListOf<Photo>()
        dataList.addAll(list)
        adapter.setData(dataList)
    }

    override fun scrollRecycler(position: Int) {
        recyclerView.layoutManager?.scrollToPosition(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            TAKE_PHOTO_REQUEST_CODE -> {
                /**
                 * получение данных из интента
                 */
                val isSuccesfull = CreatePhotoExternalScreen.getResultFromIntent(resultCode, data)

                if (isSuccesfull) {
                    presenter.photoWasMade()
                } else {
                    presenter.photoMistake()
                }
            }
        }
    }
    /**
     * Управление видимостью кнопки фото
     */
    override fun changeCreatePhotoButtonVisability(isVisible: Boolean) {
        if (isVisible) {
            createPhotoButton.visibility = View.VISIBLE
        } else {
            createPhotoButton.visibility = View.GONE
        }
    }
    /**
     * переход назад
     */
    override fun navigateBack() {
        activityNavigator.navigateBack(this)
    }

    override fun setUpMode(startType: PhotoGalleryStartMode) {
        when (startType) {
            PhotoGalleryStartMode.VIEW_MODE_WORK, PhotoGalleryStartMode.VIEW_MODE_REMARK, PhotoGalleryStartMode.VIEW_MODE_CSO -> {
                createPhotoButton.visibility = View.GONE
                adapter.deleteButtonVisibility = View.GONE
                checkImage.visibility = View.GONE
            }
            PhotoGalleryStartMode.PHOTO_MODE_WORK, PhotoGalleryStartMode.PHOTO_MODE_REMARK, PhotoGalleryStartMode.PHOTO_MODE_CSO -> {
                createPhotoButton.visibility = View.VISIBLE
                adapter.deleteButtonVisibility = View.VISIBLE
                checkImage.visibility = View.VISIBLE
            }

        }
    }
    /**
     * Управление видимостью прогресса
     */
    override fun setProgressVisibility(isVisible: Boolean) {
        if (isVisible) {
            if (progressDialog == null) {
                progressDialog = ProgressDialog()
                progressDialog!!.isCancelable = false
                progressDialog!!.show(supportFragmentManager, "")
            }
        } else {
            if (progressDialog != null) {
                progressDialog?.dismiss()
                progressDialog = null
            }
        }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisability(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Установка прогресса
     */
    override fun setSendingProgress(currentProgress: Int, maxProgress: Int) {
        progressDialog!!.setProgress(currentProgress, maxProgress)
    }
    /**
     * Переход на экран подтверждения
     */
    override fun navigateOnCheckButton() {
        navigate()
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Проверка разрешений
     */
    override fun checkPermissions() {
        cameraRtPermissionsDelegate.requestPermissions()
    }

    /**
     * Метод переопределяется наследниками, если необходима навигация, отличная от стандартной
     */
    open fun navigate() {
        navigateBack()
    }

    private fun getScreenComponent(): PhotoGalleryScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return PhotoComponent.get().photoGalleryScreenComponent()
        } else {
            return saved as PhotoGalleryScreenComponent
        }

    }

    private val rtPermissionsDelegateCallback = object : BaseRtPermissionsDelegate.Callback {
        override fun onPermissionRequestFinished(granted: Boolean) {
            if (granted) {
                presenter.permissionsGranted()
            }
        }
    }

    companion object {
        //region Extra
        private const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_CALLING_TYPE = "EXTRA_CALLING_ID"
        //endRegion

        fun getCallingIntentForWork(context: Context, remarkId: String): Intent {
            /**
             * Интент для фото работы
             */
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.PHOTO_MODE_WORK)
            return intent
        }

        fun getCallingIntentForRemark(context: Context, remarkId: String): Intent {
            /**
             * Интент для фото замечания
             */
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.PHOTO_MODE_REMARK)
            return intent
        }

        fun getCallingIntentForCso(context: Context, csoId: String): Intent {
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, csoId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.PHOTO_MODE_CSO)
            return intent
        }

        fun getCallingIntentForRemarkInViewMode(context: Context, remarkId: String): Intent {
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.VIEW_MODE_REMARK)
            return intent
        }

        fun getCallingIntentForWorkInViewMode(context: Context, remarkId: String): Intent {
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, remarkId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.VIEW_MODE_WORK)
            return intent
        }

        fun getCallingIntentForCsoInViewMode(context: Context, csoId: String): Intent {
            val intent = Intent(context, PhotoGalleryActivity::class.java)
            intent.putExtra(EXTRA_ID, csoId)
            intent.putExtra(EXTRA_CALLING_TYPE, PhotoGalleryStartMode.VIEW_MODE_CSO)
            return intent
        }

    }
}