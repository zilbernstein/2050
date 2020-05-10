package ru.digipeople.locotech.master.ui.activity.tmclist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.TMCInWork
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmclist.adapter.TmcAdapter
import ru.digipeople.locotech.master.ui.activity.tmclist.dialog.DeleteTmcDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити списка ТМЦ
 * @author Kashonkov Nikita
 */
class TmcListActivity : MvpActivity(), TmcListView {
    //region DI
    private lateinit var screenComponent: TmcListScreenComponent
    private lateinit var component: TmcListComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: TmcAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //end Region
    //region View
    private lateinit var title: TextView
    private lateinit var addBtn: ImageView
    private lateinit var backBtn: ImageView
    private lateinit var arrowBtn: ImageView
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var dialog: DeleteTmcDialog
    //end Region
    //region Other
    lateinit var presenter: TmcListPresenter
    lateinit var params: TmcListParameters
    //end Region
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        params = intent.getParcelableExtra(EXTRA_TMC_PARAMS)
        screenComponent = getScreenComponent()
        component = screenComponent.component().activityModule(ActivityModule(this)).workId(params.workId).build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmc_list)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, TmcListPresenter::class.java)
        presenter.initialize()

        title = findViewById(R.id.title)
        addBtn = findViewById(R.id.add_button)
        backBtn = findViewById(R.id.back_button)
        arrowBtn = findViewById(R.id.stock_left_arrow)

        recycler = findViewById(R.id.recycler_view)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.onDeleteClickListener = presenter::onTmcDeleteClicked
        adapter.onAmountClickListener = presenter::onAmountCicked

        dialog = DeleteTmcDialog(this)
        dialog.agreeListener = presenter::onApproveDeleteClicked
        /**
         * Обработка разворачивания по стрелке
         */
        arrowBtn.setOnClickListener {
            adapter.isRolledMode = !adapter.isRolledMode
            adapter.notifyDataSetChanged()
            if (adapter.isRolledMode) {
                arrowBtn.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_arrow_up))
            } else {
                arrowBtn.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_arrow_down))
            }
        }
        //Хардкод для дкмо
        title.text = getString(R.string.tmc_list_activity_title, params.workTitle)
        addBtn.setOnClickListener { presenter.onAddBtnClicked() }
        backBtn.setOnClickListener { presenter.onBackBtnClicked() }
    }
    /**
     * Действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        presenter.onScreenShown()
    }
    /**
     * Действия при приостановке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        navigator.navigateBack()
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<TMCInWork>) {
        adapter.setData(list)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Отображение диалога удалени
     */
    override fun showDeleteDialog() {
        dialog.show()
    }

    private fun getScreenComponent(): TmcListScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().tmcListScreenComponent()
        } else {
            return saved as TmcListScreenComponent
        }
    }

    companion object {
        //region Extras
        private const val EXTRA_TMC_PARAMS = "EXTRA_TMC_PARAMS"

        //end Region
        fun getCallingIntent(context: Context, params: TmcListParameters?): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, TmcListActivity::class.java)
            intent.putExtra(EXTRA_TMC_PARAMS, params)
            return intent
        }
    }
}