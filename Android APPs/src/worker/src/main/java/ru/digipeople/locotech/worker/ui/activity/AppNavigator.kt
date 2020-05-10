package ru.digipeople.locotech.worker.ui.activity

import android.app.Application
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import ru.digipeople.locotech.worker.ui.activity.checklist.ChecklistActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс осуществляющий навигацию между экранами
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class AppNavigator @Inject constructor(app: Application) : CoreNavigator(app) {
    fun navigateToChecklist(workId: String) {
        addCommands(ToChecklistCommand(workId))
    }

    private inner class ToChecklistCommand(private val workId: String) : Command {
        override fun apply() {
            /**
             * интент
             */
            val intent = ChecklistActivity.getCallingIntent(currentActivity, workId)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
}