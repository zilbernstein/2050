package ru.digipeople.locotech.master.ui.activity.approved

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.ActivityNavigator
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.approved.adapter.ApprovedWorkAdapter
import ru.digipeople.locotech.master.ui.activity.approved.adapter.DiffUtilCallback
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.delegate.WorkaroundFragmentDelegate
import javax.inject.Inject

/**
 * Активити согласование
 *
 * @author Kashonkov Nikita
 */
class ApprovedActivity : MvpActivity(), ApprovedView {
    //region DI
    private lateinit var screenComponent: ApprovedScreenComponent
    private lateinit var component: ApprovedComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var workaroundFragmentDelegate: WorkaroundFragmentDelegate
    @Inject
    lateinit var approvedAdapter: ApprovedWorkAdapter
    @Inject
    lateinit var unapprovedAdapter: ApprovedWorkAdapter
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //region View
    private lateinit var parentLayout: androidx.drawerlayout.widget.DrawerLayout
    private lateinit var presenter: ApprovedPresenter

    private lateinit var recyclerUnapproved: androidx.recyclerview.widget.RecyclerView
    private lateinit var recyclerApproved: androidx.recyclerview.widget.RecyclerView
    private lateinit var approveBtn: Button
    //endregion
    //region Other
    //endRegion
    /**
     * Операции, выполняемые при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.approvedComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approved)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.approved_activity_title)

        parentLayout = findViewById(R.id.drawerLayout)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.approvedPresenter() }, ApprovedPresenter::class.java)
        presenter.initialize()

        recyclerUnapproved = findViewById(R.id.activity_approved_new_recycler_view)
        recyclerUnapproved.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        unapprovedAdapter.menuItemClickListener = this::onAdapterMenuItemClicked
        recyclerUnapproved.adapter = unapprovedAdapter

        recyclerApproved = findViewById(R.id.activity_approved_approved_recycler_view)
        recyclerApproved.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        approvedAdapter.menuItemClickListener = this::onAdapterMenuItemClicked
        recyclerApproved.adapter = approvedAdapter

        approveBtn = findViewById(R.id.activity_approved_approve_button)
        approveBtn.setOnClickListener { presenter.approvedButtonClicked() }
        /**
         * Инициализация дровер делегата
         */
        mainDrawerDelegate.init(false)
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
//        super.onBackPressed()
    }
    /**
     * Операции, выполняемые при каждом старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Операции, выполняемые при каждом восстановлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Операции, выполняемые при каждой остановке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Передача в адаптер согласованных работ
     */
    override fun setDataToApprovedAdapter(list: List<Work>) {
        val diffUtilCallback = DiffUtilCallback(approvedAdapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        approvedAdapter.items = list
        diffResult.dispatchUpdatesTo(approvedAdapter)
    }
    /**
     * Передача в адаптер не согласованных работ
     */
    override fun setDataToNorApprovedAdapter(list: List<Work>) {
        val diffUtilCallback = DiffUtilCallback(unapprovedAdapter.items, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        unapprovedAdapter.items = list
        diffResult.dispatchUpdatesTo(unapprovedAdapter)
    }
    /**
     * Обработка ситуации с отсутствием работ
     */
    override fun showApproveError() {
        Toast.makeText(this, R.string.approved_activity_approve_error, Toast.LENGTH_LONG).show()
    }
    /**
     * Оповещение об ошибке
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }

    private fun getScreenComponent(): ApprovedScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().approvedScreenComponent()
        } else {
            return saved as ApprovedScreenComponent
        }
    }
    /**
     * Обработка нажатия на меню у элемента списка
     */
    private fun onAdapterMenuItemClicked(menuItemType: MenuItemType, work: Work) {
        val action = when (menuItemType) {
            MenuItemType.COMMENT -> presenter::onCommentClicked
            MenuItemType.TMC -> presenter::onTmcClicked
            MenuItemType.WORKAROUND -> this::onWorkaroundClicked
            else -> return
        }
        action.invoke(work)
    }
    /**
     * Обработка передачи работы в обходное решение
     */
    private fun onWorkaroundClicked(work: Work) = workaroundFragmentDelegate
            .show({ presenter.onWorkaroundClicked(work) })

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ApprovedActivity::class.java)
        }
    }
}