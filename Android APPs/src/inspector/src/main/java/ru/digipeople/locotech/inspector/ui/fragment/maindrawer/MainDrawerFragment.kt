package ru.digipeople.locotech.inspector.ui.fragment.maindrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.runBlocking
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.BuildConfig
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.ActivityNavigator
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
    lateinit var sessionManager: SessionManager
    //endregion
    //region Other
    private var equipmentChangesDisposable = Disposables.disposed()
    //endregion
    // region Views
    private lateinit var clientName: TextView
    private lateinit var equipmentItem: TextView
    private lateinit var inspectionItem: TextView
    private lateinit var repairBookItem: TextView
    private lateinit var checkListItem: TextView
    private lateinit var unitInfoItem: TextView
    private lateinit var messageItem: TextView
    private lateinit var phonebookItem: TextView
    private lateinit var exit: TextView
    private lateinit var version: TextView
    // endregion
    /**
     * Действия при создании фрагмента
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
        /**
         * оборудование
         */
        equipmentItem = view.findViewById(R.id.drawer_main_equipment_value)
        equipmentItem.setOnClickListener {
            activityNavigator.navigateToEquipmentActivity(requireActivity())
        }
        /**
         * переход к инспекционному контролю
         */
        inspectionItem = view.findViewById(R.id.drawer_main_inspection)
        inspectionItem.setOnClickListener {
            activityNavigator.navigateToInspectionActivity(requireActivity())
        }
        /**
         * переход к книге ремонта
         */
        repairBookItem = view.findViewById(R.id.drawer_main_repair_book)
        repairBookItem.setOnClickListener {
            activityNavigator.navigateToRepairBookActivity(requireActivity())
        }
        /**
         * переход к чеклисту
         */
        checkListItem = view.findViewById(R.id.drawer_main_check_list)
        checkListItem.setOnClickListener {
            activityNavigator.navigateToCheckListActivity(requireActivity())
        }

        unitInfoItem = view.findViewById(R.id.drawer_main_unit_info)
        unitInfoItem.setOnClickListener {
            activityNavigator.navigateToSummaryActivity(requireActivity())
        }
        /**
         * переход к сообщениям
         */
        messageItem = view.findViewById(R.id.drawer_main_message)
        messageItem.setOnClickListener {
            activityNavigator.navigateToMessageActivity(requireActivity())
        }
        /**
         * переход к телефонной книге
         */
        phonebookItem = view.findViewById(R.id.drawer_main_phonebook)
        phonebookItem.setOnClickListener {
            activityNavigator.navigateToPhoneBookActivity(requireActivity())
        }
        /**
         * разлогин
         */
        exit = view.findViewById(R.id.drawer_main_exit)
        exit.setOnClickListener {
            runBlocking { sessionManager.endSession() }
            activityNavigator.navigateToAuthActivity(requireActivity())
        }

        version = view.findViewById(R.id.drawer_main_version)
        version.text = resources.getString(R.string.main_drawer_version, BuildConfig.FULL_VERSION_NAME)

        val signInInfo = sessionManager.requireSignInInfo
        clientName = view.findViewById(R.id.drawer_main_name)
        clientName.text = getString(R.string.main_drawer_name, signInInfo.lastName, signInInfo.name)

        return view
    }

    override fun init(mainDrawerDelegate: MainDrawerDelegate) {
        // nop
    }

    override fun onDrawerOpened() {

    }

    override fun onStart() {
        super.onStart()
        subscribeToEquipmentChanges()
    }

    override fun onStop() {
        super.onStop()
        unsubscribeFromEquipmentChanges()
    }
    /**
     * переход назад
     */
    override fun navigateBack() {
        activityNavigator.navigateBack(requireActivity())
    }
    /**
     * подписка на изменения оборудования
     */
    private fun subscribeToEquipmentChanges() {
        equipmentChangesDisposable = sessionManager.equipmentChanges
                .subscribe { equipment ->
                    equipmentItem.text = equipment.name
                }
    }

    private fun unsubscribeFromEquipmentChanges() {
        equipmentChangesDisposable.dispose()
    }
}