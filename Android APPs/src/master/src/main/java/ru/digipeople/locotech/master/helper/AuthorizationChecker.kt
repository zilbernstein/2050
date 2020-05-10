package ru.digipeople.locotech.master.helper

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.locotech.core.ui.activity.auth.AuthActivity
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.ui.activity.base.BaseAuthorizationChecker

/**
 * Класс для проверки авторизации
 *
 * @author Kashonkov Nikita
 */
class AuthorizationChecker : BaseAuthorizationChecker {
    /**
     * Функция проверки успешности авторизации
     */
    override fun checkAuthorization(activity: AppCompatActivity) {
        val activityNavigator = AppComponent.get().activityNavigator()
        val thingWorxClientWorker = AppComponent.get().baseThingWorx()
        if (activity !is AuthActivity) {
            if (!thingWorxClientWorker.isConnected()) {
                /**
                 * действия в случае успешной авторизации
                 */
                activityNavigator.navigateToAuthActivity(activity)
                activity.finish()
            }
        }
    }
}