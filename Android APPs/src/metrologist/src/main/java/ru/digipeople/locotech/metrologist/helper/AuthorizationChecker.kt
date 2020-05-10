package ru.digipeople.locotech.metrologist.helper


import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.activity.base.BaseAuthorizationChecker
/**
 * Класс для проверки авторизации
 *
 * @author Kashonkov Nikita
 */
class AuthorizationChecker : BaseAuthorizationChecker {
    override fun checkAuthorization(activity: AppCompatActivity) {
//        val activityNavigator = AppComponent.get().activityNavigator()
//        val baseThingWorx = AppComponent.get().baseThingWorx()
//
//        if (activity !is AuthActivity) {
//            if (!baseThingWorx.isConnected()) {
//                activityNavigator.navigateToAuthActivity(activity)
//                activity.finish()
//            }
//        }
    }
}