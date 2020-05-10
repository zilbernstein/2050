package ru.digipeople.locotech.worker.ui.activity.tmcshortage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.TMCInWork
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.choosereason.ChooseReasonActivity
import ru.digipeople.locotech.worker.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.tmcshortage.adapter.TmcShortageAdapter
import javax.inject.Inject

/**
 * Активити недостающие ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcShortageActivity : MvpActivity(), TmcShortageView {
    //region DI
    private lateinit var screenComponent: TmcShortageScreenComponent
    private lateinit var component: TmcShortageComponent
    @Inject
    lateinit var adapter: TmcShortageAdapter
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endregion
    //region View
    private lateinit var recycler: RecyclerView
    private lateinit var checkButton: ImageButton
    private lateinit var backImage: ImageView
    //endregion
    //region Other
    private lateinit var presenter: TmcShortagePresenter
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
        setContentView(R.layout.activity_tmc_shortage)

        recycler = findViewById(R.id.activity_tmc_shortage_recycler)
        recycler.layoutManager = LinearLayoutManager(this)

        val commentParams = intent.getParcelableExtra<CommentParams>(COMMENT_PARAMS)

        checkButton = findViewById(R.id.activity_tmc_shortage_check)
        /**
         * Обрабока нажатия принять
         */
        checkButton.setOnClickListener { presenter.onCheckedPressed(commentParams) }

        backImage = findViewById(R.id.activity_tmc_shortage_back_button)
        /**
         * Обработка нажатия назад
         */
        backImage.setOnClickListener { presenter.onBackIconPressed() }
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, TmcShortagePresenter::class.java)
        presenter.initialize()

    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
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

        when (requestCode) {
            ChooseReasonActivity.START_COMMENT_REQUEST -> {
                val isSuccesfull = CommentActivity.getResultFromIntent(resultCode)
                presenter.resultReturned(isSuccesfull)
            }
        }
    }
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(allList: List<TMCInWork>, currentList: MutableList<TMCInWork>) {
        adapter.dataSet = allList
        adapter.currentSet = currentList
        adapter.itemClickListener = presenter::onItemClicked
        recycler.adapter = adapter
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Обновление данных в адаптере
     */
    override fun updateAdapter() {
        adapter.notifyDataSetChanged()
    }
    /**
     * Запрос причины остановки
     */
    override fun showCheckMistake() {
        Toast.makeText(this, R.string.declined_task_сheck_mistake, Toast.LENGTH_LONG).show()
    }

    override fun finishActivityWithResult() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getScreenComponent(): TmcShortageScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().tmcShortageScreenComponent()
        } else {
            return saved as TmcShortageScreenComponent
        }
    }

    companion object {
        //region Other
        val START_COMMENT_REQUEST = 100
        private val EXTRA_WORK_ID = "EXTRA_WORK_ID"
        const val COMMENT_PARAMS = "COMMENT_PARAMS"
        // endRegion
        fun getCallingIntent(context: Context, workId: String, commentParams: CommentParams): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, TmcShortageActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            intent.putExtra(COMMENT_PARAMS, commentParams)
            return intent
        }

        fun getResultFromIntent(resultCode: Int): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }

}