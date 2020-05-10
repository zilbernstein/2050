package ru.digipeople.locotech.master.ui.activity.status

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Активити статуса работ
 *
 * @author Kashonkov Nikita
 */
class StatusActivity : MvpActivity(), StatusView {
    //region DI
    private lateinit var screenComponent: StatusScreenComponent
    private lateinit var component: StatusComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    //region other
    //region Other
    private lateinit var presenter: StatusPresenter
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.statusComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.status_activity_title)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.statusPresenter() }, StatusPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }

    private fun getScreenComponent(): StatusScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().statusScreenComponent()
        } else {
            return saved as StatusScreenComponent
        }

    }

    companion object {
        /**
         * Интент
         */
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, StatusActivity::class.java)
        }

    }

}