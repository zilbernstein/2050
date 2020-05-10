package ru.digipeople.locotech.master.ui.activity.comment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити комментариев
 * @author Kashonkov Nikita
 */
class CommentActivity : MvpActivity(), CommentView {
    //region DI
    private lateinit var screenComponent: CommentScreenComponent
    private lateinit var component: CommentComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region View
    private lateinit var backImage: ImageView
    private lateinit var checkImage: ImageView
    private lateinit var editText: EditText
    private lateinit var title: TextView
    //endregion
    //region Other
    private lateinit var presenter: CommentPresenter
    private var callingId: Int = 0
    //endRegion
    /**
     * операции при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        callingId = intent.getIntExtra(EXTRA_CALLING_ID, 0)
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .commentParams(intent.getParcelableExtra(EXTRA_COMMENT_PARAMS))
                .callingId(callingId)
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        title = findViewById(R.id.activity_comment_title)
        /**
         * установка заголовка
         */
        if (callingId == RETURN_CALLING_ID) {
            title.setText(R.string.comment_activity_return_reason)
        } else {
            title.setText(R.string.comment_activity_input_comment)
        }
        /**
         * инициализация кнопки назад и обработчика нажатия на нее
         */
        backImage = findViewById(R.id.activity_comment_back_button)
        backImage.setOnClickListener { presenter.backButtonClicked() }

        checkImage = findViewById(R.id.activity_comment_check_button)
        editText = findViewById(R.id.activity_comment_edit_text)

        checkImage.setOnClickListener { presenter.checkButtonClicked(editText.text.toString()) }

        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CommentPresenter::class.java)
        presenter.initialize()

    }
    /**
     * операции при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * операции при возобновлении работы активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * операции при переходе активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * обработка нажатия назад
     */
    override fun navigateBack() {
        if (callingId == RETURN_CALLING_ID) {
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            super.onBackPressed()
        }
    }
    /**
     * операции при завершении работы активити
     */
    override fun finishActivity() {
        if (callingId == RETURN_CALLING_ID) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
    /**
     * установка текста в поле
     */
    override fun setText(text: String?) {
        editText.setText(text)
    }

     override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(editText, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoading(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * переход к замерам
     */
    override fun navigateToMeasure(workId: String, workName: String, workStatus: Int) {
        navigator.navigateToMeasurement(workId, workName, workStatus)
    }
    /**
     * получение экранного компонента
     */
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
        private const val EXTRA_CALLING_ID = "EXTRA_CALLING_ID"
        const val RETURN_CALLING_ID = 1
        const val REMARK_CALLING_ID = 2
        const val WORK_CALLING_ID = 3
        //endRegion
        /**
         * интент для комментария к работе
         */
        fun getCallingIntentForWorkComment(context: Context, params: CommentParams): Intent {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, WORK_CALLING_ID)
            return intent
        }
        /**
         * интент стандартный
         */
        fun getCallingIntentForResult(context: Context, params: CommentParams): Intent {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, RETURN_CALLING_ID)
            return intent
        }
        /**
         * интент для комментария к замечанию
         */
        fun getCallingIntentForRemarkComment(context: Context, params: CommentParams): Intent {
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, REMARK_CALLING_ID)
            return intent
        }
        /**
         * получение данных из  интента
         */
        fun getResultFromIntent(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }
}