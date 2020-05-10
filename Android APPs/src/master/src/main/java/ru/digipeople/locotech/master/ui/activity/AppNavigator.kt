package ru.digipeople.locotech.master.ui.activity

import android.app.Application
import android.content.Intent
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import ru.digipeople.locotech.master.ui.activity.equipment.EquipmentActivity
import ru.digipeople.locotech.master.ui.activity.ordertorepair.OrderToRepairActivity
import ru.digipeople.locotech.master.ui.activity.performance.PerformanceActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс осуществляющий навигацию между экранами
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class AppNavigator @Inject constructor(app: Application) : CoreNavigator(app) {
    /**
     * Навигация к запросу на получение ТМЦ для ремонта
     */
    fun navigateToOrderToRepair() {
        addCommands(ToOrderToRepairCommand())
    }
    /**
     * Навигация к списку локомотивов/секций
     */
    fun navigateToEquipment() {
        addCommands(ToEquipmentCommand())
    }
    /**
     * Навигация к экрану исполнение
     */
    fun navigateToPerformance() {
        addCommands(ToPerformanceCommand())
    }

    private inner class ToOrderToRepairCommand : Command {
        override fun apply() {
            /**
             * Интент для активити завяки на ремонт
             */
            val intent = OrderToRepairActivity.getCallingIntent(currentActivity)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

    private inner class ToEquipmentCommand : Command {
        override fun apply() {
            /**
             * Интент для экрана оборудование (локомотивы и секции)
             */
            val intent = EquipmentActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

    private inner class ToPerformanceCommand : Command {
        override fun apply() {
            /**
             * Интент для экрана исполнение
             */
            val intent = PerformanceActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

}