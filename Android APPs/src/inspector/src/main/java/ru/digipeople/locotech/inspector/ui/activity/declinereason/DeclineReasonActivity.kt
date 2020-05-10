package ru.digipeople.locotech.inspector.ui.activity.declinereason

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonAdapter
import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonModel
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити причин удаления замечания
 * @author Kashonkov Nikita
 */
class DeclineReasonActivity : MvpActivity(), DeclineReasonView {
    //region DI
    private lateinit var screenComponent: DeclineReasonScreenComponent
    private lateinit var component: DeclineReasonComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: DeclinedReasonAdapter
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //regionView
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    //endRegion
    //region Other
    lateinit var presenter: DeclineReasonPresenter
    //endRegion
    /**
     * Действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .remarkId(intent.getStringExtra(REMARK_ID))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declined_reason)
        /**
         * Инициализация перезентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, DeclineReasonPresenter::class.java)
        presenter.initialize()

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.onReasonClickListener = presenter::onReasonClicked
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.declined_reasson_title)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)

    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        presenter.onScreenShown()
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка данных
     */
    override fun setData(reasons: List<DeclinedReasonModel>) {
        adapter.items = reasons
    }
    /**
     * Управление видимостью лоадера
     */
    override fun showLoading(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): DeclineReasonScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().declineReasonScreenComponent()
        } else {
            return saved as DeclineReasonScreenComponent
        }
    }

    companion object {
        private const val REMARK_ID = "REMARK_ID"
        fun getCallingIntent(context: Context, remarkId: String): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, DeclineReasonActivity::class.java).apply {
                putExtra(REMARK_ID, remarkId)
            }
            return intent
        }

    }
}