package ru.digipeople.locotech.worker.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.ui.activity.ActivityNavigator
import javax.inject.Inject

/**
 * Активити для отображения сплэша
 *
 * @author Sostavkin Grisha
 */
class SplashActivity : MvpActivity(), MvpView {

    //region DI
    private lateinit var screenComponent: SplashScreenComponent
    private lateinit var component: SplashComponent
    @Inject
    lateinit var activityNavigator: ActivityNavigator
    /**
     * Действи япри старте активити
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
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Переход с ожиданием
     */
    private fun waitAndGo() {
        /**
         * Установка задержки 1с
         */
        Handler().postDelayed(Runnable { activityNavigator.navigateToShiftActivity(this) }, 1000)

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