package ru.digipeople.locotech.technologist.ui.activity.remarks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.model.Work
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.AdapterData
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.DividerItemDecoration
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.RemarksAdapter
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.works.WorksAdapter
import ru.digipeople.locotech.technologist.ui.activity.remarks.dialog.approve.ApproveDialog
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити замечаний
 */
class RemarksActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerRemarksComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragment: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var remarksRecycleView: RecyclerView
    private lateinit var worksRecyclerView: RecyclerView
    private lateinit var remarkCheckBox: ImageButton
    private lateinit var acceptApprovalBtn: Button
    private lateinit var rejectApprovalBtn: Button
    private lateinit var constraintGroup: Group
    private lateinit var chooseRemarkTextView: TextView
    //endregion
    //region Other
    private val viewModel by lazy {
        ViewModelProviders.of(this, component.viewModelFactory()).get(RemarksViewModel::class.java)
    }
    private val remarksAdapter = RemarksAdapter()
    private val worksAdapter = WorksAdapter()
    private var convertToAdapterDataDisposable = Disposables.disposed()
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remark)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.remarks_title)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        remarksRecycleView = findViewById(R.id.remark_recycler)
        remarksRecycleView.addItemDecoration(DividerItemDecoration(this))
        remarksRecycleView.layoutManager = LinearLayoutManager(this)
        remarksRecycleView.adapter = remarksAdapter
        remarksAdapter.onRemarkClickListener = viewModel::onRemarkClicked

        worksRecyclerView = findViewById(R.id.work_on_remark_recycler)
        worksRecyclerView.adapter = worksAdapter
        worksAdapter.onApproveCheckedChangeListener = viewModel::onApproveCheckedChanged
        worksAdapter.onItemClickListener = viewModel::onWorkClicked

        remarkCheckBox = findViewById(R.id.remark_checkbox)
        remarkCheckBox.setOnClickListener {
            viewModel.onSelectAllBtnClicked()
        }
        acceptApprovalBtn = findViewById(R.id.button_accept_approval)
        rejectApprovalBtn = findViewById(R.id.button_reject_approval)
        constraintGroup = findViewById(R.id.constraint_group)
        chooseRemarkTextView = findViewById(R.id.remark_choose_remark)

        /**
         * Обработка нажатия кнопок принять/отменить
         */
        acceptApprovalBtn.setOnClickListener { viewModel.onWorksAcceptBtnClicked() }
        rejectApprovalBtn.setOnClickListener { viewModel.onWorksRejectBtnClicked() }

        viewModel.apply {
            worksNoDataLiveData.observe({ lifecycle }, ::setWorksNoDataVisible)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            remarksLiveData.observe({ lifecycle }, ::setRemarks)
            worksLiveData.observe({ lifecycle }, ::setWorks)
            selectedWorksLiveData.observe({ lifecycle }, ::updateSelectedState)
            allAreSelectedLiveData.observe({ lifecycle }, ::setAllSelected)
            loadingLiveData.observe({ lifecycle }, ::setLoading)
            buttonsEnabledLiveData.observe({ lifecycle }, ::setButtonsEnabled)
            acceptWorksApproveLiveData.observe({ lifecycle }, ::showAcceptApproveDialog)
            rejectWorksApproveLiveData.observe({ lifecycle }, ::showRejectApproveDialog)

            start()
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.remark_menu, menu)
        return true
    }
    /**
     * Обработка выбора пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_photos -> {
                viewModel.onPhotosMenuItemSelected()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        convertToAdapterDataDisposable.dispose()
        super.onDestroy()
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
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
    }
    /**
     * Установка замечаний
     */
    private fun setRemarks(remarks: AdapterData) {
        remarksAdapter.adapterData = remarks
    }
    /**
     * Установка работ
     */
    private fun setWorks(works: List<Work>) {
        worksAdapter.items = works
        remarksAdapter.notifyDataSetChanged()
    }
    /**
     * Управление видимостью лоадера
     */
    private fun setLoading(loading: Boolean) {
        loadingFragment.setLoadingVisibility(loading)
    }
    /**
     * Диалог подтверждения принятия
     */
    private fun showAcceptApproveDialog(works: List<String>) {
        showApproveDialog(works, false, viewModel::onAcceptWorksApproveBtnClicked)
    }
    /**
     * Диалог отклонения работ
     */
    private fun showRejectApproveDialog(works: List<String>) {
        showApproveDialog(works, true, viewModel::onRejectWorksApproveBtnClicked)
    }
    /**
     * Обновление состояния элемента
     */
    private fun updateSelectedState(selectedWorks: Map<String, Work>) {
        worksAdapter.updateSelectedState(selectedWorks)
    }
    /**
     * Выбрать все
     */
    private fun setAllSelected(allAreSelected: Boolean) {
        remarkCheckBox.isSelected = allAreSelected
    }
    /**
     * Отображение сообщения нет данных
     */
    private fun setWorksNoDataVisible(visible: Boolean) {
        chooseRemarkTextView.isInvisible = !visible
        constraintGroup.isInvisible = visible
    }
    /**
     * Отображение ошибки пользователя
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(remarksRecycleView, userError.message, Snackbar.LENGTH_LONG).show()
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        acceptApprovalBtn.setEnabled(enabled)
        rejectApprovalBtn.setEnabled(enabled)
    }
    /**
     * Создание диалога
     */
    private fun showApproveDialog(works: List<String>, reject: Boolean, onOkBtnClickListener: () -> Unit) {
        supportFragmentManager.findFragmentByTag(ApproveDialog.TAG) as ApproveDialog?
                ?: ApproveDialog.newInstance(works, reject)
                        .apply {
                            this.onOkBtnClickListener = onOkBtnClickListener
                        }
                        .show(supportFragmentManager, ApproveDialog.TAG)
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, RemarksActivity::class.java)
        }
    }
}