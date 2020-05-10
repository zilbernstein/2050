package ru.digipeople.locotech.technologist.ui.activity.comment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити комментария
 *
 * @author Sostavkin Grisha
 */
class CommentActivity : MvpActivity(), CommentView {
    //region DI
    private lateinit var screenComponent: CommentScreenComponent
    private lateinit var component: CommentComponent
    @Inject
    lateinit var loadingFragment: LoadingFragmentDelegate
    @Inject
    lateinit var navigator: Navigator
    //endregion
    //region View
    private lateinit var backImage: ImageView
    private lateinit var checkImage: ImageView
    private lateinit var editText: EditText
    private lateinit var title: TextView
    //endregion
    //region Other
    private lateinit var presenter: CommentPresenter
    //endRegion
    /**
     * Действия при создании  активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .commentParams(intent.getParcelableExtra(EXTRA_COMMENT_PARAMS))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comm)

        title = findViewById(R.id.activity_comment_title)
        /**
         * Установка заголовка
         */
        title.setText(R.string.comment_activity_text_hint)

        backImage = findViewById(R.id.activity_comment_back_button)
        backImage.setOnClickListener { presenter.backButtonClicked() }

        checkImage = findViewById(R.id.activity_comment_check_button)
        checkImage.setOnClickListener { presenter.checkButtonClicked(editText.text.toString()) }

        editText = findViewById(R.id.activity_comment_edit_text)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CommentPresenter::class.java)
        presenter.initialize()

    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }

    override fun onResume() {
        navigator.onResume(this)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        navigator.onPause()
    }
    /**
     * Установка текста
     */
    override fun setText(text: String) {
        editText.setText(text)
        editText.requestFocus()
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(editText, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoading(isLoading: Boolean) {
        loadingFragment.setLoadingVisibility(isLoading)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    private fun getScreenComponent(): CommentScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        return when (saved == null) {
            true -> AppComponent.get().commentScreenComponent()
            false -> saved as CommentScreenComponent
        }
    }

    companion object {
        //region Extras
        private val EXTRA_COMMENT_PARAMS = "EXTRA_COMMENT_PARAMS"

        //endRegion
        fun getCallingIntent(context: Context, params: CommentParams): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, CommentActivity::class.java)
            intent.putExtra(EXTRA_COMMENT_PARAMS, params)
            return intent
        }
    }
}