package ru.digipeople.locotech.inspector.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.ActivityNavigator
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.mvp.view.MvpView
import javax.inject.Inject

/**
 * Активити экрана заставки
 * @author Kashonkov Nikita
 */
class SplashActivity : MvpActivity(), MvpView {
    //region DI
    private lateinit var screenComponent: SplashScreenComponent
    private lateinit var component: SplashComponent
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.splashComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        waitAndGo()
    }
    /**
     * переход назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }

    private fun waitAndGo() {
        /**
         * добавляем задержку 1с
         */
        Handler().postDelayed(Runnable { activityNavigator.navigateToEquipmentActivity(this) }, 1000)

    }

    private fun getScreenComponent(): SplashScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().splashScreenComponent()
        } else {
            return saved as SplashScreenComponent
        }

    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * Интент
             */
            return Intent(context, SplashActivity::class.java)
        }

    }
}