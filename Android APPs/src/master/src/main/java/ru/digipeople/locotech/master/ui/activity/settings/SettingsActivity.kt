package ru.digipeople.locotech.master.ui.activity.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Активити настроек
 *
 * @author Kashonkov Nikita
 */
class SettingsActivity : MvpActivity(), SettingsView {

    //region DI
    private lateinit var screenComponent: SettingsScreenComponent
    private lateinit var component: SettingsComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    //region other
    //region Other
    private lateinit var presenter: SettingsPresenter
    private lateinit var recyler: RecyclerView
    //endRegion
    /**
     * Действия при созданием активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.settingsComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_settings)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.settings_activity_title)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.settingsPresenter() }, SettingsPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация бокового меню
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

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    private fun getScreenComponent(): SettingsScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().settingsScreenComponent()
        } else {
            return saved as SettingsScreenComponent
        }

    }

    companion object {
        /**
         * интент
         */
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }

    }

}