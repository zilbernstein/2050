package ru.digipeople.locotech.worker.ui.fragment.maindrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.digipeople.ui.UiMainDrawerView
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.BuildConfig
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.data.task.CurrentTaskProvider
import ru.digipeople.locotech.worker.data.task.TaskStatus
import ru.digipeople.locotech.worker.helper.ClientProvider
import ru.digipeople.locotech.worker.ui.activity.ActivityNavigator
import ru.digipeople.locotech.worker.ui.activity.AppNavigator
import javax.inject.Inject

/**
 * Боковое меню.
 *
 * @author Aleksandr Brazhkin
 */
class MainDrawerFragment : Fragment(), UiMainDrawerView {

    //region Di
    private lateinit var component: MainDrawerComponent
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var appNavigator: AppNavigator
    @Inject
    lateinit var taskProvider: CurrentTaskProvider
    @Inject
    lateinit var clientProvider: ClientProvider
    //endregion
    // region Views
    private lateinit var nameText: TextView
    private lateinit var statusText: TextView
    private lateinit var myTask: TextView
    private lateinit var taskLayout: ConstraintLayout
    private lateinit var currentTask: TextView
    private lateinit var message: TextView
    private lateinit var phones: TextView
    private lateinit var settings: TextView
    private lateinit var exit: TextView
    private lateinit var version: TextView
    private lateinit var planBik: TextView
    // endregion
    /**
     * действия при создании фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = DaggerMainDrawerComponent.builder()
                .appComponent(AppComponent.get())
                .build()
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.view_drawer_main, container, false)

        nameText = view.findViewById(R.id.drawer_main_name)
        /**
         * нажатие на имя
         */
        nameText.setOnClickListener {
            activityNavigator.navigateToShiftActivity(requireActivity())
        }

        statusText = view.findViewById(R.id.drawer_main_status)
        myTask = view.findViewById(R.id.drawer_main_my_task)
        /**
         * нажатие на задачи
         */
        myTask.setOnClickListener {
            activityNavigator.navigateToMytaskActivity(requireActivity())
        }

        taskLayout = view.findViewById(R.id.drawer_main_task_layout)
        /**
         * нажатие на конкретную задачу
         */
        taskLayout.setOnClickListener {
            val currentTask = taskProvider.task
            if (currentTask != null) {
                activityNavigator.navigateToTaskActivityFromMenu(requireActivity(), taskProvider.task!!.id)
            } else {
                Toast.makeText(activity, R.string.main_drawer_task_mistake, Toast.LENGTH_LONG).show()
            }
        }

        currentTask = view.findViewById(R.id.drawer_main_current_task)
        settings = view.findViewById(R.id.drawer_main_settings)/**
         * переход к экрану настроек
         */
        settings.setOnClickListener {
            Toast.makeText(activity, R.string.main_drawer_settings_mistake, Toast.LENGTH_LONG).show()
        }

        message = view.findViewById(R.id.drawer_main_message)
        /**
         * переход к экрану сообщенгий
         */
        message.setOnClickListener {
            activityNavigator.navigateToMessageActivity(requireActivity())
        }

        phones = view.findViewById(R.id.drawer_main_telephone_book)
        /**
         * переход к экрану телефонной книги
         */
        phones.setOnClickListener {
            activityNavigator.navigateToTelephoneBookActivity(requireActivity())
        }

        planBik = view.findViewById(R.id.drawer_main_plan_bik)
        /**
         * переход к экрану планБик
         */
        planBik.setOnClickListener {
            appNavigator.navigateToPlanBik()
        }
        /**
         * разлогин
         */
        exit = view.findViewById(R.id.drawer_main_exit)
        exit.setOnClickListener {
            activityNavigator.navigateToAuthActivity(requireActivity())
        }

        version = view.findViewById(R.id.drawer_main_version)

        version.text = resources.getString(R.string.main_drawer_version, BuildConfig.FULL_VERSION_NAME)
        val client = clientProvider.client
        nameText.text = getString(R.string.main_drawer_name, client.lastName, client.name)

        return view
    }

    override fun init(mainDrawerDelegate: MainDrawerDelegate) {
        // nop
    }

    override fun onDrawerOpened() {
        setUpDrawerView()
    }
    /**
     * переход к предыдущему экрану
     */
    override fun navigateBack() {
        activityNavigator.navigateBack(requireActivity())
    }
    /**
     * Установка данных в боковом меню
     */
    private fun setUpDrawerView() {
        /**
         * раскраска статуса
         */
        if (clientProvider.client.isInShift) {
            setStatusColor(R.color.appGreen)
            setStatusText(R.string.main_drawer_status_work)
        } else {
            setStatusColor(R.color.appGray)
            setStatusText(R.string.main_drawer_status_not_in_work
            )
        }
        val currentTask = taskProvider.task
        if (currentTask == null) {
            setCurrentTask(getString(R.string.main_drawer_task_unchosen))
            setCurrentTaskTextColor(R.color.appGray)
        } else {
            /**
             * раскраска задания о статуса
             */
            setCurrentTask(currentTask.id)
            when (currentTask.status) {
                TaskStatus.NEW -> {
                    setCurrentTaskTextColor(R.color.blueButton)
                }
                TaskStatus.PAUSED -> {
                    setCurrentTaskTextColor(R.color.appYellow)
                }
                TaskStatus.IN_WORK -> {
                    setCurrentTaskTextColor(R.color.appGreen)
                }
                TaskStatus.DECLINED -> {
                    setCurrentTaskTextColor(R.color.appRed)
                }
            }
        }
    }
    /**
     * установка статуса
     */
    fun setStatusText(resource: Int) {
        statusText.setText(resource)
    }
    /**
     * установка цвета статуса
     */
    fun setStatusColor(color: Int) {
        statusText.background = ContextCompat.getDrawable(requireContext(), color)
    }
    /**
     * установка текущего задания
     */
    fun setCurrentTask(task: String) {
        currentTask.text = task
    }
    /**
     * установка цвета текущего задания
     */
    fun setCurrentTaskTextColor(color: Int) {
        currentTask.setTextColor(ContextCompat.getColor(requireContext(), color))
    }
}