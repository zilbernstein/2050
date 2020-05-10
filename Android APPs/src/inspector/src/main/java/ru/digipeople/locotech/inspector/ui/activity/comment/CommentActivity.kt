package ru.digipeople.locotech.inspector.ui.activity.comment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.inspection.StartTab
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity

/**
 * Активити комментариев
 *
 * @author Kashonkov Nikita
 */
class CommentActivity : MvpActivity(), CommentView {
    //region DI
    private lateinit var screenComponent: CommentScreenComponent
    private lateinit var component: CommentComponent
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
    private var startTab = StartTab.CYCLIC_WORK
    //endRegion
    /**
     * Действия при создании активити
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

        if (callingId == 1) {
            title.setText(R.string.comment_activity_return_reason)
        } else {
            title.setText(R.string.comment_activity_input_comment)
        }
        /**
         * Обработка нажатия назад
         */
        backImage = findViewById(R.id.activity_comment_back_button)
        backImage.setOnClickListener { presenter.backButtonClicked() }
        /**
         * Обработка нажатия принять
         */
        checkImage = findViewById(R.id.activity_comment_check_button)
        checkImage.setOnClickListener {
            finishActivity()
            presenter.checkButtonClicked(editText.text.toString())
        }

        editText = findViewById(R.id.activity_comment_edit_text)

        val startTypeCode = intent.getIntExtra(EXTRA_START_TYPE, StartTab.CYCLIC_WORK.code)
        startTab = StartTab.valueOf(startTypeCode)!!
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CommentPresenter::class.java)
        presenter.initialize()
        presenter.setStartTab(startTab)

    }

    override fun onStart() {
        super.onStart()
    }
    /**
     * переход назад
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
     * Действия при завершении активити
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
     * установка текста комментария
     */
    override fun setText(text: String) {
        editText.removeTextChangedListener(textWatcher)
        editText.setText(text)
        editText.setSelection(text.length)
        editText.addTextChangedListener(textWatcher)
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
        private const val EXTRA_CALLING_ID = "EXTRA_CALLING_ID"
        private const val EXTRA_START_TYPE = "EXTRA_START_TYPE"
        const val RETURN_CALLING_ID = 1
        const val REMARK_CALLING_ID = 2
        const val WORK_CALLING_ID = 3
        const val CSO_CALLING_ID = 4
        //endRegion
        fun getCallingIntentForWorkComment(context: Context, params: CommentParams): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, WORK_CALLING_ID)
            return intent
        }

        fun getCallingIntentForResult(context: Context, params: CommentParams): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, RETURN_CALLING_ID)
            return intent
        }

        fun getCallingIntentForRemarkComment(context: Context, params: CommentParams, startTab: Int): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, REMARK_CALLING_ID)
            intent.putExtra(EXTRA_START_TYPE, startTab)
            return intent
        }

        fun getCallingIntentForCsoComment(context: Context, params: CommentParams): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            intent.putExtra(EXTRA_CALLING_ID, CSO_CALLING_ID)
            return intent
        }

        fun getResultFromIntent(resultCode: Int): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }
    /**
     * Обработка изменения текста
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.onCommentChange(s.toString())
        }

    }
}