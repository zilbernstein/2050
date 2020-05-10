package ru.digipeople.locotech.worker.ui.activity.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.MeasurementStatus
import ru.digipeople.locotech.worker.model.MeasurementsTask
import ru.digipeople.locotech.worker.model.WorkDetail
import ru.digipeople.locotech.worker.model.WorkStatus
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.choosereason.ChooseReasonActivity
import ru.digipeople.locotech.worker.ui.activity.photo.PhotoActivity
import ru.digipeople.locotech.worker.ui.activity.task.adapter.TMCAdapter
import ru.digipeople.locotech.worker.ui.activity.task.adapter.TMCDecoration
import ru.digipeople.locotech.worker.ui.activity.task.adapter.TMCLinearLayoutManager
import ru.digipeople.locotech.worker.ui.activity.task.dialog.MeasurementTaskDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.widget.setForceShowIcon
import ru.digipeople.utils.DateUtils
import javax.inject.Inject

/**
 * Активити задания
 *
 * @author Kashonkov Nikita
 */
class TaskActivity : MvpActivity(), TaskView {
    //region DI
    private lateinit var screenComponent: TaskScreenComponent
    lateinit var component: TaskComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var adapter: TMCAdapter
    //endRegion
    //regionView
    lateinit var parentLayout: DrawerLayout
    lateinit var statusText: TextView
    lateinit var timeText: TextView
    lateinit var percentText: TextView
    lateinit var photoIcon: ImageView
    lateinit var taskProgress: ProgressBar
    lateinit var equipmentNumberText: TextView
    lateinit var equipmentProgress: ProgressBar
    lateinit var sectionTitleText: TextView
    lateinit var taskTitleText: TextView
    lateinit var taskStatusImage: ImageView
    lateinit var tmcRecycler: androidx.recyclerview.widget.RecyclerView
    lateinit var leftButton: ImageView
    lateinit var rightButton: ImageView
    lateinit var middleButton: ImageView
    lateinit var controllLayout: ConstraintLayout
    lateinit var repeatsCount: TextView
    lateinit var commentText: TextView
    lateinit var commentGroup: Group
    lateinit var menu: ImageView
    lateinit var shadowView: View
    lateinit var workers: TextView
    lateinit var workersTitle: TextView
    lateinit var timeNormal: TextView
    lateinit var measurementsStatusBtn: Button
    lateinit var measurementsStatus: TextView
    lateinit var measurementsStatusGroup: Group
    //endRegion
    //region Other
    private lateinit var presenter: TaskPresenter
    private lateinit var taskActivtityAutorefreshDelegate: TaskActivityAutorefreshDelegate
    private lateinit var workDetail: WorkDetail
    private lateinit var workId: String
    private var isCallingFromMenu: Boolean = false
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        workId = intent.getStringExtra(EXTRA_WORK_ID)
        component = screenComponent.componentBuilder().activityModule(ActivityModule(this))
                .workId(workId).build()

        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        isCallingFromMenu = false
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.task_activity_title)
        /**
         * инициализация меню
         */
        if (isCallingFromMenu) {
            mainDrawerDelegate.init(true)
        } else {
            mainDrawerDelegate.init(false)
        }

        parentLayout = findViewById(R.id.drawerLayout)
        statusText = findViewById(R.id.activity_task_status_value)
        timeText = findViewById(R.id.activity_task_time)
        percentText = findViewById(R.id.activity_task_percent)
        taskProgress = findViewById(R.id.activity_task_task_progress_in_work)
        equipmentNumberText = findViewById(R.id.activity_task_equipment_number)
        equipmentProgress = findViewById(R.id.activity_task_equipment_progress)
        sectionTitleText = findViewById(R.id.activity_task_section_title)
        taskTitleText = findViewById(R.id.activity_task_task_title)
        taskStatusImage = findViewById(R.id.activity_task_task_status)
        tmcRecycler = findViewById(R.id.activity_task_tms_list)
        leftButton = findViewById(R.id.activity_task_left_controll_button)
        rightButton = findViewById(R.id.activity_task_right_controll_button)
        middleButton = findViewById(R.id.activity_task_middle_control_button)
        controllLayout = findViewById(R.id.activity_task_control_controll_layout)
        commentText = findViewById(R.id.activity_task_comment)
        commentGroup = findViewById(R.id.activity_task_comment_group)
        repeatsCount = findViewById(R.id.repeats_count)
        shadowView = findViewById(R.id.shadow_view)
        workers = findViewById(R.id.workers)
        workersTitle = findViewById(R.id.activity_task_workers_title)
        timeNormal = findViewById(R.id.activity_task_time_normal)
        measurementsStatusGroup = findViewById(R.id.activity_task_measure_status_group)
        measurementsStatusBtn = findViewById(R.id.activity_task_measure_status_btn)
        measurementsStatus = findViewById(R.id.activity_task_measure_status)

        menu = findViewById(R.id.task_menu)
        /**
         * обработка нажатия на меню
         */
        menu.setOnClickListener {
            val popupMenu = PopupMenu(this, menu)
            popupMenu.inflate(R.menu.task_menu)
            popupMenu.setForceShowIcon(true)
            /**
             * добавлоение пунктов в меню
             */
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.photos -> navigator.navigateToPhoto(workId)
                    R.id.measures -> navigator.navigateToMeasure(workId, workDetail.workName, workDetail.workStatus)
                }

                true
            }
            popupMenu.show()
        }
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter(component::presenter, TaskPresenter::class.java)
        presenter.initialize()

        taskActivtityAutorefreshDelegate = TaskActivityAutorefreshDelegate(Handler())
        taskActivtityAutorefreshDelegate.listener = this::setTime

        tmcRecycler.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
        taskActivtityAutorefreshDelegate.onStop()
    }
    /**
     * обработка нажатия назад
     */
    override fun onBackPressed() {
        if (isCallingFromMenu) {
//            super.onBackPressed()
        } else {
            navigator.navigateBack()
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    override fun showBlankView(isVisible: Boolean) {
        shadowView.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            START_PAUSE_REASON_REQUEST -> {
                val reasonId = ChooseReasonActivity.getResultFromIntent(resultCode, data)
                if (reasonId != null) {
                    presenter.pauseReasonResultReturned(reasonId)
                }
            }
            DONE_WORK_PHOTO_REQUEST -> {
                val result = PhotoActivity.getExtra(resultCode)
                if (result) {
                    presenter.onCheckButtonClicked()
                }
            }
        }
    }
    /**
     * установка данных на экране в зависимости от статуса работы
     */
    override fun setUpTaskView(workDetail: WorkDetail) {
        this.workDetail = workDetail
        setUpCommonView(workDetail)
        setUpProgress(workDetail)
        setUpMeasurementsStatus(workDetail.measurementStatus)
        when (workDetail.workStatus) {
            WorkStatus.IN_WORK -> setUpInActiveTask()
            WorkStatus.IN_TASK -> setUpNewTask()
            WorkStatus.PAUSED -> setUpPausedTask()
            WorkStatus.DONE -> setUpDoneTask()
        }
    }
    /**
     * отображение ошибки
     */
    override fun showMistake(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * отобрадение ошибки начала работы
     */
    override fun showStartShiftMistake() {
        Snackbar.make(parentLayout, R.string.task_activity_start_shift_error, Snackbar.LENGTH_LONG).show()
    }
    /**
     * управлние видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * щапрос на проведение аппаратных замеров
     */
    override fun setMeasurementsTask(task: MeasurementsTask) {
        showTaskInfoDialog(task)
    }
    /**
     * отобразить ошибки чеклиста
     */
    override fun showChecklistError() {
        Snackbar.make(tmcRecycler, getString(R.string.task_activity_checklist_error), Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): TaskScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().taskScreenComponent()
        } else {
            return saved as TaskScreenComponent
        }
    }
    /**
     * диалог о создании задание на проведение апп замеров
     */
    private fun showTaskInfoDialog(task: MeasurementsTask) {
        AlertDialog.Builder(this)
                .setTitle(R.string.task_activity_measurement_task_title)
                .setOnDismissListener { setUpMeasurementsStatus(task.measurementsStatus) }
                .setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        task.measurementsDevices)) { _, _ -> }
                .show()
    }
    /**
     * установка сатуса проведения апп замара
     */
    private fun setUpMeasurementsStatus(status: MeasurementStatus) {
        measurementsStatusGroup.visibility = View.VISIBLE
        when (status) {
            MeasurementStatus.NO_TASK -> {
                measurementsStatus.text = getString(R.string.task_activity_measurement_no_task)
                measurementsStatusBtn.text = getString(R.string.task_activity_measurement_status_btn_assign)
                measurementsStatusBtn.setOnClickListener { showMeasurementsTaskDialog() }
            }
            MeasurementStatus.WAITING -> {
                measurementsStatus.text = getString(R.string.task_activity_measurement_waiting)
                measurementsStatusBtn.text = getString(R.string.task_activity_measurement_status_btn_check_receive)
                measurementsStatusBtn.setOnClickListener { presenter.onStatusBtnClicked(workId, workDetail.workName, workDetail.workStatus) }
            }
            MeasurementStatus.RECEIVED -> {
                measurementsStatus.text = getString(R.string.task_activity_measurement_received)
                measurementsStatusBtn.text = getString(R.string.task_activity_measurement_status_btn_assign)
                measurementsStatusBtn.setOnClickListener { showMeasurementsTaskDialog() }
            }
        }
    }
    /**
     * установка статуса
     */
    override fun setMeasurementsStatus(status: MeasurementStatus) {
        setUpMeasurementsStatus(status)
    }
    /**
     * ошибка чеклиста
     */
    override fun showChecklistCheckError() {
        val snackbar = Snackbar.make(tmcRecycler, R.string.task_activity_checklist_checked_error, Snackbar.LENGTH_LONG)
        val tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        tv.maxLines = 2
        snackbar.show()
    }
    /**
     * диалог запроса апп замера
     */
    private fun showMeasurementsTaskDialog() {
        MeasurementTaskDialog.show(supportFragmentManager) { stage, sectionNumber ->
            presenter.onAssignmentParamsApplied(stage, sectionNumber)
        }
    }
    /**
     * новая работа
     */
    private fun setUpNewTask() {
        statusText.setText(R.string.task_activity_status_new_work)
        statusText.setTextColor(ContextCompat.getColor(this, R.color.appBlue))

        taskProgress.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress_bar_green_with_white_back))

        rightButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_start))
        rightButton.setOnClickListener { presenter.onStartButtonClicked() }

        leftButton.visibility = View.GONE
        middleButton.setOnClickListener { presenter.onChecklistBtnClicked(intent.getStringExtra(EXTRA_WORK_ID)) }

        taskStatusImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_new_work))
    }
    /**
     * работа на паузе
     */
    private fun setUpPausedTask() {
        statusText.setText(R.string.task_activity_status_paused)
        statusText.setTextColor(ContextCompat.getColor(this, R.color.appGray))

        taskProgress.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress_bar_yellow_with_white_back))

        leftButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_controll_check))
        leftButton.setOnClickListener { presenter.onCheckChecklist(workId) }
        rightButton.setOnClickListener { presenter.onStartButtonClicked() }
        rightButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_start))
        middleButton.setOnClickListener { presenter.onChecklistBtnClicked(intent.getStringExtra(EXTRA_WORK_ID)) }

        taskStatusImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_paused_work))

        taskActivtityAutorefreshDelegate.onStop()
    }
    /**
     * задание в работе
     */
    private fun setUpInActiveTask() {
        statusText.setText(R.string.task_activity_status_in_work)
        statusText.setTextColor(ContextCompat.getColor(this, R.color.appGreen))

        taskProgress.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress_bar_green_with_white_back))

        taskStatusImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_in_work))

        leftButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_controll_check))
        leftButton.setOnClickListener { presenter.onCheckChecklist(workId) }
        rightButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_pause))
        rightButton.setOnClickListener { presenter.onPauseButtonClicked() }
        middleButton.setOnClickListener { presenter.onChecklistBtnClicked(intent.getStringExtra(EXTRA_WORK_ID)) }

        leftButton.visibility = View.VISIBLE

        taskActivtityAutorefreshDelegate.onStart()
    }
    /**
     * выполненная работа
     */
    private fun setUpDoneTask() {
        statusText.setText(R.string.task_activity_status_done)
        statusText.setTextColor(ContextCompat.getColor(this, R.color.appGreen))

        taskProgress.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.progress_bar_green_with_white_back))

        taskStatusImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_in_work))

        leftButton.visibility = View.GONE
        middleButton.setOnClickListener { presenter.onChecklistBtnClicked(intent.getStringExtra(EXTRA_WORK_ID)) }
        rightButton.visibility = View.GONE
        taskActivtityAutorefreshDelegate.onStop()
    }
    /**
     * установка данных по работе
     */
    private fun setUpCommonView(workDetail: WorkDetail) {
        equipmentNumberText.text = getString(R.string.task_activity_equipment_title, workDetail.equipmentName, workDetail.equipmentNumber)
        repeatsCount.text = "${workDetail.repeats}"
        /**
         * раскраска прогресса по работе
         */
        when (workDetail.equipmentProgress) {
            in 0..90 -> {
                equipmentProgress.progressDrawable = ContextCompat.getDrawable(this, R.drawable.green_progress_bar_drawable)
            }
            in 90..94 -> {
                equipmentProgress.progressDrawable = ContextCompat.getDrawable(this, R.drawable.yellow_progress_bar_drawable)
            }
            else -> {
                equipmentProgress.progressDrawable = ContextCompat.getDrawable(this, R.drawable.red_progress_bar_drawable)
            }
        }

        equipmentProgress.setProgress(workDetail.equipmentProgress)

        taskTitleText.text = workDetail.workName

        val layoutManager = TMCLinearLayoutManager(this)
        tmcRecycler.layoutManager = layoutManager

        val dividerItemDecoration = TMCDecoration(ContextCompat.getDrawable(this, R.drawable.gray_divider)!!)
        tmcRecycler.addItemDecoration(dividerItemDecoration)

        adapter.setData(workDetail.camList)

        if (workDetail.comment != null && workDetail.comment!!.text.isNotEmpty()) {
            commentGroup.visibility = View.VISIBLE
            commentText.text = workDetail.comment!!.text

        } else {
            commentGroup.visibility = View.GONE
        }
        /**
         * отображение исполнителей
         */
        workersTitle.text = getString(R.string.task_activity_workers, workDetail.workers.size)
        val workersNames = workDetail.workers.joinToString(separator = "   ") { it.name }
        workers.text = workersNames

        timeNormal.text = getString(R.string.task_activity_time_normal, DateUtils.convertToString(workDetail.timeLimit))
    }
    /**
     * установка прогресса
     */
    fun setUpProgress(workDetail: WorkDetail) {
        timeText.text = DateUtils.convertToString(workDetail.timeRemain)

        val progress = (workDetail.timeLimit - workDetail.timeRemain) * 100 / workDetail.timeLimit
        taskProgress.setProgress(progress.toInt())

        percentText.text = getString(R.string.task_activity_percent, progress)
    }
    /**
     * установка времени
     */
    private fun setTime(timeDelta: Long) {
        workDetail.timeRemain -= timeDelta
        setUpProgress(workDetail)
    }

    companion object {
        //region Extras
        private val EXTRA_WORK_ID = "EXTRA_WORK_ID"
        const val START_PAUSE_REASON_REQUEST = 101
        const val DONE_WORK_PHOTO_REQUEST = 102
        //endregion

        fun getCallingIntent(context: Context, workId: String): Intent {
            /**
             * интент
             */
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            return intent
        }
    }
}