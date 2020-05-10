package ru.digipeople.locotech.technologist.helper

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.locotech.core.ui.activity.auth.AuthActivity
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.ui.activity.base.BaseAuthorizationChecker

/**
 * Класс для проверки авторизации
 *
 * @author Kashonkov Nikita
 */
class AuthorizationChecker : BaseAuthorizationChecker {
    override fun checkAuthorization(activity: AppCompatActivity) {
        val activityNavigator = AppComponent.get().activityNavigator()
        val thingWorxClientWorker = AppComponent.get().baseThingWorx()
        /**
         * Проверка активити авторизации
         */
        if (activity !is AuthActivity) {
            if (!thingWorxClientWorker.isConnected()) {
                activityNavigator.navigateToAuthActivity(activity)
                activity.finish()
            }
        }
    }
}