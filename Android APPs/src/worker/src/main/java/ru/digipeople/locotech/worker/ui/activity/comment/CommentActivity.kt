package ru.digipeople.locotech.worker.ui.activity.comment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.WorkStatus
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.measurements.MeasureParams
import ru.digipeople.locotech.worker.ui.activity.photo.PhotoActivity
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити комментария
 *
 * @author Kashonkov Nikita
 */
class CommentActivity : MvpActivity(), CommentView {
    //region DI
    private lateinit var screenComponent: CommentScreenComponent
    private lateinit var component: CommentComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragment: LoadingFragmentDelegate
    //endregion
    //region View
    private lateinit var backImage: ImageView
    private lateinit var checkImage: ImageView
    private lateinit var editText: EditText
    private lateinit var title: TextView
    //endregion
    //region Extras
    private lateinit var workStatus: WorkStatus
    private var editableText = true
    //endregion
    //region Other
    private lateinit var presenter: CommentPresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .commentParams(intent.getParcelableExtra(EXTRA_COMMENT_PARAMS))
                .build()
        component.inject(this)
        /**
         * Получение данных из интента
         */
        intent?.extras?.apply {
            workStatus = get(EXTRA_WORK_STATUS) as WorkStatus? ?: WorkStatus.IN_WORK
            editableText = getBoolean(EXTRA_EDITABLE_TEXT, true)
        }

        val measuresParams = intent.getParcelableExtra<MeasureParams>(EXTRA_INDICATOR)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        /**
         * Определени версии андроид
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.appGray)
        }

        title = findViewById(R.id.activity_comment_title)
        /**
         * Установка заголовка
         */
        title.setText(R.string.comment_activity_input_comment)

        backImage = findViewById(R.id.activity_comment_back_button)
        backImage.setOnClickListener { presenter.backButtonClicked() }

        editText = findViewById(R.id.activity_comment_edit_text)
        checkImage = findViewById(R.id.activity_comment_check_button)

        if (editableText) {
            checkImage.setOnClickListener {
                if (measuresParams == null)
                    presenter.checkButtonClicked(editText.text.toString())
            }
        } else {
            editText.setEnabled(false)
            checkImage.setVisibility(View.INVISIBLE)
            title.setText("Комментарий")
        }
        /**
         * Инициализацуия презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CommentPresenter::class.java)
        presenter.initialize()

    }
    /**
     * Получение комментария
     */
    private fun getComment(): String {
        return editText.text.toString()
    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия при возобновлениии активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при приостановке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Переход на предыдущий экран
     */
    override fun navigateBack() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_PAUSE_WORK_PHOTO) {
            val result = PhotoActivity.getExtra(resultCode)
            if (result) {
                presenter.photoResultReturned()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    /**
     * Переход к фото
     */
    override fun navigateToPhoto(taskId: String) {
        navigator.navigateToPhotoForResult(taskId, RC_PAUSE_WORK_PHOTO)
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoading(isLoading: Boolean) {
        loadingFragment.setLoadingVisibility(isLoading)
    }

    override fun finishActivityWithResult() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    /**
     * Установить текст
     */
    override fun setText(text: String) {
        editText.setText(text)
        editText.requestFocus()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(editText, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): CommentScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().commentScreenComponent()
        } else {
            return saved as CommentScreenComponent
        }
    }

    companion object {
        //region Extras
        private const val EXTRA_COMMENT_PARAMS = "EXTRA_COMMENT_PARAMS"
        private const val EXTRA_INDICATOR = "INDICATOR"
        private const val EXTRA_WORK_STATUS = "EXTRA_WORK_STATUS"
        private const val EXTRA_EDITABLE_TEXT = "EXTRA_EDITABLE_TEXT"
        //endregion
        //region Rc
        private const val RC_PAUSE_WORK_PHOTO = 102
        //endregion

        fun getCallingIntent(context: Context, params: CommentParams): Intent {
            /**
             * Интент обычный
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            return intent
        }

        fun getCallingIntentFromMeasure(context: Context, params: CommentParams, isEditable: Boolean, workStatus: WorkStatus): Intent {
            /**
             * Интент для перехода с замеров
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_WORK_STATUS, workStatus)
            intent.putExtra(EXTRA_EDITABLE_TEXT, isEditable)
            return intent
        }

        fun getResultFromIntent(resultCode: Int): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }
}