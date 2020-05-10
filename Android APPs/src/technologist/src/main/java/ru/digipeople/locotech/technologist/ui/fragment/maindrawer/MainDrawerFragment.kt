package ru.digipeople.locotech.technologist.ui.fragment.maindrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.locotech.technologist.BuildConfig
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.helper.ClientProvider
import ru.digipeople.locotech.technologist.ui.ActivityNavigator
import ru.digipeople.thingworx.BaseThingWorx
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
    lateinit var baseThingWorx: BaseThingWorx
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    @Inject
    lateinit var clientProvider: ClientProvider
    //endregion
    // region Views
    private lateinit var telephoneBookItem: TextView
    private lateinit var messageItem: TextView
    private lateinit var remarksItem: TextView
    private lateinit var exit: TextView
    private lateinit var name: TextView
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
        val view = inflater.inflate(R.layout.fragment_main_drawer, container, false)

        remarksItem = view.findViewById(R.id.drawer_main_remarks)
        /**
         * Переход к замечаниям
         */
        remarksItem.setOnClickListener {
            activityNavigator.navigateToRemarkActivity(requireActivity())
        }

        messageItem = view.findViewById(R.id.drawer_main_messages)
        /**
         * Переход к сообщениям
         */
        messageItem.setOnClickListener {
            activityNavigator.navigateToMessageActivity(requireActivity())
        }

        telephoneBookItem = view.findViewById(R.id.drawer_main_telephone_book)
        /**
         * Переход к телефонной книге
         */
        telephoneBookItem.setOnClickListener {
            activityNavigator.navigateToTelephoneBookActivity(requireActivity())
        }

        exit = view.findViewById(R.id.drawer_main_exit)
        /**
         * Разлогин
         */
        exit.setOnClickListener {
            baseThingWorx.disconnectBlocking()
            activityNavigator.navigateToAuthActivity(requireActivity())
        }
        /**
         * Отображение версии и имени
         */
        version = view.findViewById(R.id.drawer_main_version)
        version.text = resources.getString(R.string.main_drawer_version, BuildConfig.FULL_VERSION_NAME)

        name = view.findViewById(R.id.drawer_main_name)
        val client = clientProvider.client
        name.text = getString(R.string.main_drawer_name, client.lastName, client.name)

        return view
    }

    override fun init(mainDrawerDelegate: MainDrawerDelegate) {
        // nop
    }

    override fun onDrawerOpened() {
        // nop
    }
    /**
     * переход на предыдущий экран
     */
    override fun navigateBack() {
        activityNavigator.navigateBack(requireActivity())
    }
}