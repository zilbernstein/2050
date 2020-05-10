package ru.digipeople.locotech.metrologist.ui.fragment.maindrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.metrologist.AppComponent
import ru.digipeople.locotech.metrologist.BuildConfig
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.helper.session.SessionManager
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.MeasurementTypesActivity
import ru.digipeople.locotech.metrologist.ui.activity.reports.ReportsActivity
import ru.digipeople.locotech.metrologist.ui.activity.sections.SectionsActivity
import ru.digipeople.message.ui.activity.messagelist.MessageListActivity
import ru.digipeople.telephonebook.ui.activity.telephonedirectory.TelephoneDirectoryActivity
import ru.digipeople.ui.UiMainDrawerView
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.utils.AppSchedulers
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
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var navigator: AppNavigator
    //endregion
    // region Views
    private lateinit var userName: TextView
    private lateinit var currentSection: TextView
    // endregion
    //region Other
    private var currentSectionChangesDisposable = Disposables.disposed()
    //endregion
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

        userName = view.findViewById(R.id.main_drawer_user_name)
        currentSection = view.findViewById(R.id.main_drawer_current_section)
        currentSection.isSelected = true
        /**
         * переход к секциям
         */
        view.findViewById<View>(R.id.main_drawer_sections).setOnClickListener {
            if (activity !is SectionsActivity) {
                navigator.navigateToSections()
            }
        }
        /**
         * переход к замерам
         */
        view.findViewById<View>(R.id.main_drawer_measurements).setOnClickListener {
            if (activity !is MeasurementTypesActivity) {
                navigator.navigateToMeasurementTypes()
            }
        }
        /**
         * Переход к отчетам
         */
        view.findViewById<View>(R.id.main_drawer_reports).setOnClickListener {
            if (activity !is ReportsActivity) {
                navigator.navigateToReports()
            }
        }
        /**
         * Переход к сообщениям
         */
        view.findViewById<View>(R.id.main_drawer_messages).setOnClickListener {
            if (activity !is MessageListActivity) {
                navigator.navigateToMessage()
            }
        }
        /**
         * Переход к телефонной книге
         */
        view.findViewById<View>(R.id.main_drawer_phones).setOnClickListener {
            if (activity !is TelephoneDirectoryActivity) {
                navigator.navigateToPhoneBook()
            }
        }
        /**
         * переход к авторизации
         */
        view.findViewById<View>(R.id.main_drawer_exit).setOnClickListener {
            sessionManager.endSession()
            navigator.navigateToAuth()
        }
        /**
         * данные пользователя
         */
        sessionManager.authInfo?.let {
            userName.text = getString(R.string.main_drawer_user_name, it.lastName, it.firstName)
        }
        /**
         * версия
         */
        view.findViewById<TextView>(R.id.main_drawer_version).text =
                getString(R.string.main_drawer_version, BuildConfig.FULL_VERSION_NAME)

        return view
    }
    /**
     * инициализация
     */
    override fun init(mainDrawerDelegate: MainDrawerDelegate) {
        // nop
    }

    override fun onDrawerOpened() {
        // nop
    }

    override fun navigateBack() {
        // nop
    }

    override fun onStart() {
        super.onStart()
        subscribeToCurrentSectionChanges()
    }

    override fun onStop() {
        unsubscribeFromCurrentSectionChanges()
        super.onStop()
    }
    /**
     * подписка на изменение секции
     */
    private fun subscribeToCurrentSectionChanges() {
        currentSectionChangesDisposable = sessionManager.currentSectionChanges
                .observeOn(AppSchedulers.ui())
                .subscribe {
                    val sectionName = sessionManager.currentSectionName
                    currentSection.text = if (sectionName.isBlank()) {
                        getString(R.string.main_drawer_section_not_selected)
                    } else {
                        sectionName
                    }
                }
    }

    private fun unsubscribeFromCurrentSectionChanges() {
        currentSectionChangesDisposable.dispose()
    }
}