package ru.digipeople.locotech.technologist.ui.activity

import android.app.Application
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигатор
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class AppNavigator @Inject constructor(app: Application) : CoreNavigator(app) {

}