package ru.digipeople.locotech.master.ui.activity.remark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Remark
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.ActivityNavigator
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.locotech.master.ui.activity.remark.adapter.remark.RemarkAdapter
import ru.digipeople.locotech.master.ui.activity.remark.adapter.work.WorkAdapter
import ru.digipeople.locotech.master.ui.activity.tmclist.dialog.DeleteWorkDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.delegate.WorkaroundFragmentDelegate
import ru.digipeople.utils.DateUtils
import javax.inject.Inject

/**
 * Активити замечаний
 * @author Kashonkov Nikita
 */
class RemarkActivity : MvpActivity(), RemarkView {
    private val F_TAG_SETTINGS_DIALOG = "F_TAG_SETTINGS_DIALOG"
    //region DI
    private lateinit var screenComponent: RemarkScreenComponent
    private lateinit var component: RemarkComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var workaroundFragmentDelegate: WorkaroundFragmentDelegate
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var workAdapter: WorkAdapter
    @Inject
    lateinit var remarkAdapter: RemarkAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var navigator: Navigator
    //region View
    private lateinit var workList: androidx.recyclerview.widget.RecyclerView
    private lateinit var remarklList: androidx.recyclerview.widget.RecyclerView
    private lateinit var timeView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var percent: TextView
    private lateinit var createRemarkBtn: FloatingActionButton
    private lateinit var createWorkBtn: FloatingActionButton
    private lateinit var deleteWorkDialog: DeleteWorkDialog
    //ensregion
    //region Other
    private lateinit var presenter: RemarkPresenter
    private lateinit var autoRefreshDelegate: RemarkAutoRefreshDelegate
    private var equipmentTimeLeft = 0L
    private var equipmentTimeRequired = 0L
    //endRegion
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locomotive_details)

        createRemarkBtn = findViewById(R.id.activity_locomotive_detail_create_remark)
        createRemarkBtn.setOnClickListener { presenter.createRemarkClicked() }

        createWorkBtn = findViewById(R.id.activity_locomotive_detail_create_work)
        createWorkBtn.setOnClickListener { presenter.createWorkClicked() }
        /**
         * Инициализация тулбара и добавление заголовка
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(getString(R.string.locomotive_details_title))

        workList = findViewById(R.id.activity_locomotive_details_work_recycler)
        val layoutManager = LinearLayoutManager(this)
        workList.layoutManager = layoutManager

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_divider)!!)
        workList.addItemDecoration(dividerItemDecoration)
        /**
         * Инициализация ресайклера
         */
        remarklList = findViewById(R.id.activity_locomotive_detail_remark_recycler)
        val remarkLayoutManager = LinearLayoutManager(this)
        remarklList.layoutManager = remarkLayoutManager
        /**
         * добавление оформления к списку
         */
        val remarkDividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        remarkDividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_divider)!!)
        remarklList.addItemDecoration(remarkDividerItemDecoration)

        timeView = findViewById(R.id.locomotive_detail_time)
        progressBar = findViewById(R.id.locomotive_detail_progress_bar)
        percent = findViewById(R.id.locomotive_detail_percent)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, RemarkPresenter::class.java)
        presenter.initialize()

        autoRefreshDelegate = RemarkAutoRefreshDelegate(Handler())
        autoRefreshDelegate.listener = this::setTime
        /**
         * создание диалога при удалении работы
         */
        deleteWorkDialog = DeleteWorkDialog(this)
        deleteWorkDialog.agreeListener = presenter::onDeleteWorkAgreeBtnClicked

        workAdapter.menuItemClickListener = this::onWorkAdapterMenuItemClicked

        remarklList.adapter = remarkAdapter
        remarkAdapter.itemClickLocomotiveDetailsRemarkAdapter = presenter::onRemarkItemClicked
        remarkAdapter.menuItemClickListener = this::onRemarkAdapterMenuItemClicked

        workList.adapter = workAdapter
        /**
         * Инициализация меню
         */
        mainDrawerDelegate.init(false)
    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия при возобновлении работы активити
     */
    override fun onResume() {
        super.onResume()
        workAdapter.notifyDataSetChanged()
        navigator.onResume(this)
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
//        super.onBackPressed()
    }
    /**
     * Обработка перехода активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * обрабокта остановки активити
     */
    override fun onStop() {
        autoRefreshDelegate.onStop()
        super.onStop()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка данных по работам
     */
    override fun setDataToWorkAdapter(list: List<Work>, shouldSavePosition: Boolean) {
        if (shouldSavePosition) {
            val layoutManager = workList.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            val position = layoutManager.findFirstVisibleItemPosition()
            workAdapter.setData(list)
            if (position <= list.size) {
                workList.scrollToPosition(position)
            }
        } else {
            workAdapter.setData(list)
        }
    }
    /**
     * Установка данных по замечаниям
     */
    override fun setDataToRemarkAdapter(currentRemark: Remark, list: List<Remark>, shouldSavePosition: Boolean) {
        remarkAdapter.setCurrentRemark(currentRemark)
        if (shouldSavePosition) {
            val layoutManager = remarklList.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            val position = layoutManager.findFirstVisibleItemPosition()
            remarkAdapter.setData(list)
            if (position <= list.size) {
                remarklList.scrollToPosition(position)
            }
        } else {
            remarkAdapter.setData(list)
        }
    }
    /**
     * Управление видимостью данных
     */
    override fun setDataVisibility(isVisible: Boolean) {
        if (isVisible) {
            remarklList.visibility = View.VISIBLE
            workList.visibility = View.VISIBLE
        } else {
            remarklList.visibility = View.GONE
            workList.visibility = View.GONE
        }
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(timeView, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка прогресса
     */
    override fun setEquipmentProgress(progress: Int, leftTime: Long, requiredTime: Long) {
        equipmentTimeLeft = leftTime
        equipmentTimeRequired = requiredTime

        timeView.text = DateUtils.convertToString(equipmentTimeLeft)
//        val progressPercent = ((requiredTime - leftTime) * 100) / requiredTime

        progressBar.setProgress(progress)
//        percent.text = getString(R.string.remark_activity_percent, progressPercent)
        autoRefreshDelegate.onStart()
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * установка выбранного замечания
     */
    fun setCurrentRemark(remark: Remark) {
    }
    /**
     * Отображение диалога удаления работы
     */
    override fun showDeleteDialog() {
        deleteWorkDialog.show()
    }
    /**
     * Обработка нажатия на меню у работы
     */
    private fun onWorkAdapterMenuItemClicked(menuItemType: MenuItemType, work: Work) {
        val action = when (menuItemType) {
            MenuItemType.COMMENT -> presenter::showWorkComment
            MenuItemType.TMC -> presenter::tmcClicked
            MenuItemType.DELETE -> presenter::deleteWorkItemClicked
            MenuItemType.WORKAROUND -> this::onWorkaroundClicked
            else -> return
        }
        action.invoke(work)
    }
    /**
     * Обработка обходного решения
     */
    private fun onWorkaroundClicked(work: Work) {
        workaroundFragmentDelegate.show({ presenter.onWorkaroundClicked(work) })
    }
    /**
     * Инициализация тулбара
     */
    private fun onRemarkAdapterMenuItemClicked(menuItemType: MenuItemType, remark: Remark) {
        val action = when (menuItemType) {
            MenuItemType.COMMENT -> presenter::onCommentClicked
            MenuItemType.PHOTO -> presenter::onPhotoClicked
            MenuItemType.DELETE -> presenter::deleteRemarkClicked
            else -> return
        }
        action.invoke(remark)
    }

    private fun getScreenComponent(): RemarkScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().locomotiveDetailScreenComponent()
        } else {
            return saved as RemarkScreenComponent
        }
    }
    /**
     * Установка времени
     */
    private fun setTime(timeDelta: Long) {
        equipmentTimeLeft -= timeDelta

        timeView.text = DateUtils.convertToString(equipmentTimeLeft)
//        val progressPercent = ((equipmentTimeRequired - equipmentTimeLeft) * 100) / equipmentTimeRequired
//        percent.text = getString(R.string.remark_activity_percent, progressPercent)
    }

    companion object {
        /**
         * интент
         */
        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, RemarkActivity::class.java)
            return intent
        }

    }
}