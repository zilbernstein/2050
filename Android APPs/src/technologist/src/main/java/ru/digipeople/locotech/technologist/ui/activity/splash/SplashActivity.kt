package ru.digipeople.locotech.technologist.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.ui.ActivityNavigator
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.mvp.view.MvpView
import javax.inject.Inject

/**
 * Сплэш активити
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
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.splashComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
    /**
     * Действия при восстановлении активити
     */
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
     * Добавление задержки 1с
     */
    private fun waitAndGo() {

        Handler().postDelayed(Runnable { activityNavigator.navigateToRemarkActivity(this) }, 1000)

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
            return Intent(context, SplashActivity::class.java)
        }

    }
}