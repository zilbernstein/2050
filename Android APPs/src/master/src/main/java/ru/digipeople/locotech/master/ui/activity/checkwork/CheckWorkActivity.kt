package ru.digipeople.locotech.master.ui.activity.checkwork

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.checkwork.adapter.CheckWorkAdapter
import ru.digipeople.locotech.master.ui.activity.performance.adapter.DiffUtilCallback
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
class CheckWorkActivity : MvpActivity(), CheckWorkView {

    //region DI
    private lateinit var screenComponent: CheckWorkScreenComponent
    private lateinit var component: CheckWorkComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: CheckWorkAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //region View
    private lateinit var recyler: androidx.recyclerview.widget.RecyclerView
    private lateinit var alertText: AppCompatTextView
    private lateinit var button: Button
    //endregion
    //region Other
    private lateinit var presenter: CheckWorkPresenter
    private var callingType: CheckWorkCallingType? = null
    //endRegion
    /**
     * Операции при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        val callingId = intent.getIntExtra(EXTRA_CALLING_ID, 0)
        callingType = CheckWorkCallingType.valueOf(callingId)
        /**
         * Инициализщация экранного компонента
         */
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .callingType(callingType!!)
                .build()
        component.inject(this)

        if(callingType == CheckWorkCallingType.CALLING_WORK_ACCEPT){
            adapter.isPhotoVisible = true
            adapter.isPerformersVisible = false
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_work)
        /**
         * Инициализация тулбара и меню
         */
        toolbarDelegate.init()
        mainDrawerDelegate.init(true)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CheckWorkPresenter::class.java)
        presenter.initialize()

        alertText = findViewById(R.id.check_work_allert_text)
        button = findViewById(R.id.check_work_btn)
        button.setOnClickListener { presenter.buttonClicked() }
        /**
         * определение типа
         */
        when (callingType) {
            /**
             * согласование
             */
            CheckWorkCallingType.CALLING_WORK_APPROVE -> {
                setUpView(R.string.check_work_title_approve, R.string.check_work_allert_approve, R.string.check_work_button_approve)
            }
            /**
             * старт работы
             */
            CheckWorkCallingType.CALLING_WORK_START -> {
                setUpView(R.string.check_work_title_run, R.string.check_work_allert_run, R.string.check_work_button_run)
            }
            /**
             * прием работы
             */
            CheckWorkCallingType.CALLING_WORK_ACCEPT -> {
                setUpView(R.string.check_work_title_run, R.string.check_work_allert_aссept, R.string.check_work_button_aссept)
            }
        }
        /**
         * инициализация ресайклера
         */
        recyler = findViewById(R.id.check_work_recycler_view)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyler.layoutManager = layoutManager
        adapter.deleteClickListener = presenter::deleteButtonClicked
        adapter.photoClickListener = presenter::onPhotoButtonClicked
        recyler.adapter = adapter
    }
    /**
     * Операции при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Операции при пробуждении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Операции при переходе активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }
    /**
     * установка данных
     */
    override fun setData(list: List<Work>) {
        val diffUtilCallback = DiffUtilCallback(adapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        adapter.items = list
        diffResult.dispatchUpdatesTo(adapter)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recyler, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }

    private fun getScreenComponent(): CheckWorkScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().checkWorkScreenComponent()
        } else {
            return saved as CheckWorkScreenComponent
        }
    }
    /**
     * установка заголовков экрана
     */
    private fun setUpView(title: Int, alertTitle: Int, btnTitle: Int) {
        toolbarDelegate.setTitle(title)
        alertText.setText(alertTitle)
        button.setText(btnTitle)
    }

    companion object {
        //region Extras
        private val EXTRA_CALLING_ID = "EXTRA_CALLING_ID"
        val CALLING_WORK_PERFORMANCE = 2
        //endregion
        /**
         * базовый интент
         */
        fun getCallingIntent(context: Context, callingId: Int): Intent {
            val intent = Intent(context, CheckWorkActivity::class.java)
            intent.putExtra(EXTRA_CALLING_ID, callingId)
            return intent
        }
        /**
         * интент для операции согласования
         */
        fun getCallingIntentForApprove(context: Context): Intent {
            val intent = Intent(context, CheckWorkActivity::class.java)
            intent.putExtra(EXTRA_CALLING_ID, CheckWorkCallingType.CALLING_WORK_APPROVE.value)
            return intent
        }
        /**
         * интент для операции старта работ
         */
        fun getCallingIntentForStart(context: Context): Intent {
            val intent = Intent(context, CheckWorkActivity::class.java)
            intent.putExtra(EXTRA_CALLING_ID, CheckWorkCallingType.CALLING_WORK_START.value)
            return intent
        }
        /**
         * интент для принятия работ
         */
        fun getCallingIntentForAccept(context: Context): Intent {
            val intent = Intent(context, CheckWorkActivity::class.java)
            intent.putExtra(EXTRA_CALLING_ID, CheckWorkCallingType.CALLING_WORK_ACCEPT.value)
            return intent
        }
    }
}
