package ru.digipeople.locotech.inspector.ui.activity.controlpoint

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.ActivityNavigator
import ru.digipeople.locotech.inspector.ui.activity.controlpoint.adapter.ControlPoint
import ru.digipeople.locotech.inspector.ui.activity.controlpoint.adapter.ControlPointAdapter
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити контрольных точек
 *
 * @author Kashonkov Nikita
 */
class ControlPointActivity : MvpActivity(), ControlPointView {
    //region DI
    private lateinit var screenComponent: ControlPointScreenComponent
    private lateinit var component: ControlPointComponent
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var adapter: ControlPointAdapter
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //end Region
    //region View
    private lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var title: TextView
    //end Region
    //region Other
    private lateinit var presenter: ControlPointPresenter
    //end Region
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component()
                .activityModule(ActivityModule(this))
                .workId(intent.getStringExtra(EXTRA_WORK_ID))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_point)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, ControlPointPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter = adapter

        title = findViewById(R.id.title)
    }
    /**
     * переход назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        presenter.onScreenShown()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установить заголовок
     */
    override fun setTitle(workName: String) {
        title.text = workName
    }
    /**
     * Установить название секции
     */
    override fun setSectionName(sectionName: String) {
        toolbarDelegate.setTitle(getString(R.string.control_point_activity_title, sectionName))
    }
    /**
     * Установка данных
     */
    override fun setData(items: List<ControlPoint>) {
        adapter.items = items
    }
    /**
     * Управление видимостью лоадера
     */
    override fun showLoading(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Показать ошибки
     */
    override fun showError(error: UserError) {
        Snackbar.make(recyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): ControlPointScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        return if (saved == null) {
            AppComponent.get().controlPointScreenComponent()
        } else {
            saved as ControlPointScreenComponent
        }
    }

    companion object {
        private const val EXTRA_WORK_ID = "EXTRA_WORK_ID"

        fun getCallingIntent(context: Context, workId: String): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, ControlPointActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            return intent
        }
    }
}