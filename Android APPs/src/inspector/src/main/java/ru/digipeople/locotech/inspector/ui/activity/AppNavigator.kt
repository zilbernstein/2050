package ru.digipeople.locotech.inspector.ui.activity

import android.app.Application
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс осуществляющий навигацию между экранами
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class AppNavigator @Inject constructor(app: Application) : CoreNavigator(app) {

}