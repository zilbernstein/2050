package ru.digipeople.locotech.master.ui.fragment.maindrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.runBlocking
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.BuildConfig
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.locotech.master.ui.activity.ActivityNavigator
import ru.digipeople.locotech.master.ui.activity.AppNavigator
import ru.digipeople.locotech.master.ui.dialog.selectequipment.SelectEquipmentDialog
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.UiMainDrawerView
import ru.digipeople.ui.activity.base.MainDrawerDelegate
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
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var subscriptionProvider: SubscriptionProvider
    //endregion
    // region Views
    private lateinit var clientName: TextView
    private lateinit var equipmentValueItem: TextView
    private lateinit var equipmentItem: TextView
    private lateinit var statusItem: TextView
    private lateinit var approvanceItem: TextView
    private lateinit var assignmentItem: TextView
    private lateinit var workersPresenceItem: TextView
    private lateinit var orderToRepairItem: TextView
    private lateinit var perfomanceItem: TextView
    private lateinit var equipmentGroup: Group
    private lateinit var urgencyItem: ConstraintLayout
    private lateinit var alertCountText: TextView
    private lateinit var messageItem: TextView
    private lateinit var settingsItem: TextView
    private lateinit var telephoneBookItem: TextView
    private lateinit var exit: TextView
    private lateinit var version: TextView
    private lateinit var planBik: TextView
    // endregion
    //region Other
    private var equipmentChangesDisposable = Disposables.disposed()
    private var urgentDisposable = Disposables.disposed()
    private lateinit var mainDrawerDelegate: MainDrawerDelegate
    private var selectEquipmentDialog: SelectEquipmentDialog? = null
    //endregion
    /**
     * Действия при создани фрагмента
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

        equipmentValueItem = view.findViewById(R.id.drawer_main_equipment_value)
        /**
         * Обработка нажатия на оборудование
         */
        equipmentValueItem.setOnClickListener {
            if (selectEquipmentDialog == null) {
                mainDrawerDelegate.closeDrawer()
                val dialog = SelectEquipmentDialog.newInstance()
                dialog.onDismissListener = { selectEquipmentDialog = null }
                dialog.show(requireFragmentManager(), null)
                selectEquipmentDialog = dialog
            }
        }
        /**
         * Обработка нажатия на пункты меню
         */
        equipmentItem = view.findViewById(R.id.drawer_main_equipment)
        equipmentItem.setOnClickListener {
            appNavigator.navigateToEquipment()
        }
        /**
         * Замечания
         */
        statusItem = view.findViewById(R.id.drawer_main_status)
        statusItem.setOnClickListener {
            activityNavigator.navigateToLocomotiveDetailsActivity(requireActivity())
        }
        /**
         * Согласование
         */
        approvanceItem = view.findViewById(R.id.drawer_main_approvance)
        approvanceItem.setOnClickListener {
            activityNavigator.navigateToApprovedActivity(requireActivity())
        }
        /**
         * Групповое назначение
         */
        assignmentItem = view.findViewById(R.id.drawer_main_assignment)
        assignmentItem.setOnClickListener {
            activityNavigator.navigateToAssignmentActivity(requireActivity())
        }
        /**
         * Явка сотрудников
         */
        workersPresenceItem = view.findViewById(R.id.drawer_main_workers_presence)
        workersPresenceItem.setOnClickListener {
            activityNavigator.navigateToWorkersPresenceActivity(requireActivity())
        }
        /**
         * Заявка на ремонт
         */
        orderToRepairItem = view.findViewById(R.id.drawer_main_order_to_repair)
        orderToRepairItem.setOnClickListener {
            appNavigator.navigateToOrderToRepair()
        }
        /**
         * Исполнение
         */
        perfomanceItem = view.findViewById(R.id.drawer_main_perfomance)
        perfomanceItem.setOnClickListener {
            appNavigator.navigateToPerformance()
        }

        equipmentGroup = view.findViewById(R.id.drawer_main_equipment_group)
        /**
         * Срочно
         */
        alertCountText = view.findViewById(R.id.drawer_alert_text)
        urgencyItem = view.findViewById(R.id.drawer_main_urgency)
        urgencyItem.setOnClickListener {
            activityNavigator.navigateToUrgentActivity(requireActivity())
        }
        /**
         * Сообщения
         */
        messageItem = view.findViewById(R.id.drawer_main_message)
        messageItem.setOnClickListener {
            activityNavigator.navigateToMessageActivity(requireActivity())
        }
        /**
         * Настройки
         */
        settingsItem = view.findViewById(R.id.drawer_main_settings)
        settingsItem.setOnClickListener {
            activityNavigator.navigateToSettingsActivity(requireActivity())
        }
        /**
         * Телефонная книга
         */
        telephoneBookItem = view.findViewById(R.id.drawer_main_telephone_book)
        telephoneBookItem.setOnClickListener {
            activityNavigator.navigateToPhoneBookActivity(requireActivity())
        }

        planBik = view.findViewById(R.id.drawer_main_plan_bik)
        planBik.setOnClickListener {
            appNavigator.navigateToPlanBik()
        }
        /**
         * Выход
         */
        exit = view.findViewById(R.id.drawer_main_exit)
        exit.setOnClickListener {
            runBlocking { sessionManager.endSession() }
            activityNavigator.navigateToAuthActivity(requireActivity())
        }

        version = view.findViewById(R.id.drawer_main_version)
        version.text = resources.getString(R.string.main_drawer_version, BuildConfig.FULL_VERSION_NAME)

        val signInInfo = sessionManager.signInInfo
        clientName = view.findViewById(R.id.drawer_main_name)
        clientName.text = getString(R.string.main_drawer_name, signInInfo.lastName, signInInfo.firstName)

        version = view.findViewById(R.id.drawer_main_version)

        return view
    }
    /**
     * Инициализация
     */
    override fun init(mainDrawerDelegate: MainDrawerDelegate) {
        this.mainDrawerDelegate = mainDrawerDelegate
    }

    override fun onDrawerOpened() {
        setAlertCount(mainDrawerDelegate.urgentAmount)
    }
    /**
     * Переход на предыдущий экран
     */
    override fun navigateBack() {
        activityNavigator.navigateBack(requireActivity())
    }
    /**
     * Действия при старте
     */
    override fun onStart() {
        super.onStart()
        setEquipment()
        subscribeToUrgent()
        subscribeToEquipmentChanges()
    }

    override fun onPause() {
        // Закрываем всегда диалог при уходе с экрана,
        // потому что почти везде в onStart начинается загрузка данных,
        // и поверх выскакивает ProgressDialog
        selectEquipmentDialog?.dismiss()
        super.onPause()
    }

    override fun onStop() {
        unsubscribeFromEquipmentChanges()
        urgentDisposable.dispose()
        super.onStop()
    }

    private fun setEquipment() {
    }
    /**
     * Подписка на срочно
     */
    private fun subscribeToUrgent() {
        urgentDisposable.dispose()
        urgentDisposable = subscriptionProvider.urgentAmountSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ amount ->
                    mainDrawerDelegate.urgentAmount = amount
                    if (amount > 0) {
                        if (!mainDrawerDelegate.iconBack) {
                            mainDrawerDelegate.toolbarDelegate.setAlertCount(amount!!)
                        }
                        setAlertCount(amount)
                    } else {
                        mainDrawerDelegate.toolbarDelegate.hideAlertCount()
                    }
                })
    }
    /**
     * Установка числа предупреждений
     */
    fun setAlertCount(count: Int) {
        if (count > 99) {
            alertCountText.setText(R.string.maximum_alert_count)
        } else {
            alertCountText.text = "" + count
        }
    }

    fun setAlertCountVisability(visable: Boolean) {
        if (visable) {
            alertCountText.visibility = View.VISIBLE
        } else {
            alertCountText.visibility = View.GONE
        }
    }
    /**
     * Подписка на изменение оборужования
     */
    private fun subscribeToEquipmentChanges() {
        equipmentChangesDisposable = sessionManager.equipmentChanges
                .subscribe {
                    equipmentValueItem.text = sessionManager.selectedEquipment?.name ?: ""
                    equipmentGroup.isVisible = sessionManager.selectedEquipment != null

                    val endDrawable: Int?
                    val isClickable: Boolean
                    if (sessionManager.availableEquipments.size > 1) {
                        endDrawable = R.drawable.ic_switch_equipment
                        isClickable = true
                    } else {
                        endDrawable = 0
                        isClickable = false
                    }
                    equipmentValueItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, endDrawable, 0)
                    equipmentValueItem.isClickable = isClickable
                }
    }
    /**
     * Отписка от изменения оборудования
     */
    private fun unsubscribeFromEquipmentChanges() {
        equipmentChangesDisposable.dispose()
    }
}