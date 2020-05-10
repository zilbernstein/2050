package ru.digipeople.locotech.worker.ui.activity.choosereason

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.PauseReason
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.choosereason.adapter.ReasonAdapter
import ru.digipeople.locotech.worker.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.tmcshortage.TmcShortageActivity
import javax.inject.Inject

/**
 * Активити выбор причины остановки работы
 *
 * @author Kashonkov Nikita
 */
class ChooseReasonActivity : MvpActivity(), ChooseReasonView {
    //region DI
    private lateinit var screenComponent: ChooseReasonScreenComponent
    private lateinit var component: ChooseReasonComponent
    @Inject
    lateinit var adapter: ReasonAdapter
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region View
    private lateinit var recycler: RecyclerView
    private lateinit var backImage: ImageView
    //endregion
    //region Other
    private lateinit var presenter: ChooseReasonPresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .workId(intent.getStringExtra(EXTRA_WORK_ID))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reason)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, ChooseReasonPresenter::class.java)
        presenter.initialize()

        recycler = findViewById(R.id.activity_reason_recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.itemClickListener = presenter::onItemClicked

        backImage = findViewById(R.id.activity_reason_back_button)
        backImage.setOnClickListener { presenter.onBackIconPressed() }

    }
    /**
     * Дейчствия при старте активити
     */
    override fun onStart() {
        super.onStart()
        val commentParams = intent.getParcelableExtra<CommentParams>(COMMENT_PARAMS)
        presenter.onScreenShown(commentParams)
    }
    /**
     * Действия при возобновлении активити
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

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**
         * Выбор действия
         */
        when (requestCode) {
            START_COMMENT_REQUEST -> {
                val isSuccesfull = CommentActivity.getResultFromIntent(resultCode)
                presenter.resultReturned(isSuccesfull)
            }
            START_TMC_SHORTAGE_REQUEST -> {
                val isSuccesfull = TmcShortageActivity.getResultFromIntent(resultCode)
                presenter.resultReturned(isSuccesfull)
            }
        }
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<PauseReason>) {
        adapter.setData(list)
    }
    //    override fun navigateToComment(commentParams: CommentParams) {
//        activityNavigator.navigateToCommentActivity(this, commentParams, START_COMMENT_REQUEST)
//    }

//    override fun navigateToChooseTMC(taskId: String) {
//        activityNavigator.navigateToTmcShortageActivity(this, START_TMC_SHORTAGE_REQUEST)
//    }

    override fun finishActivityWithResult(reasonId: String) {
        val intent = Intent()
        intent.putExtra(REASON_ID, reasonId)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошиюбки
     */
    override fun showError(message: String) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): ChooseReasonScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().chooseReasonScreenComponent()
        } else {
            return saved as ChooseReasonScreenComponent
        }
    }

    companion object {
        //region Other
        val START_COMMENT_REQUEST = 100
        val START_TMC_SHORTAGE_REQUEST = 101
        private val EXTRA_WORK_ID = "EXTRA_WORK_ID"
        const val REASON_ID = "REASON_ID"
        const val COMMENT_PARAMS = "COMMENT_PARAMS"
        //endRegion
        fun getCallingIntent(context: Context, workId: String, commentParams: CommentParams): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, ChooseReasonActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            intent.putExtra(COMMENT_PARAMS, commentParams)
            return intent
        }
        /**
         * Получение данных из интента
         */
        fun getResultFromIntent(resultCode: Int, intent: Intent?): String? {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    return intent.getStringExtra(REASON_ID)
                } else {
                    return null
                }
            } else {
                return null
            }
        }
    }

}