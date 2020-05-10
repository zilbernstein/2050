package ru.digipeople.locotech.metrologist.ui.activity

import android.app.Application
import android.content.Intent
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import ru.digipeople.locotech.metrologist.ui.activity.editmeasurement.EditMeasurementActivity
import ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.MeasurementConfirmationActivity
import ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.MeasurementDetailActivity
import ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.MeasurementTypesActivity
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.MeasurementWheelPairsActivity
import ru.digipeople.locotech.metrologist.ui.activity.profilometers.ProfilometersActivity
import ru.digipeople.locotech.metrologist.ui.activity.reports.ReportsActivity
import ru.digipeople.locotech.metrologist.ui.activity.sections.SectionsActivity
import ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.TuningReasonsActivity
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.telephonebook.ui.activity.telephonedirectory.TelephoneDirectoryActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс осуществляющий навигацию между экранами
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class AppNavigator @Inject constructor(app: Application, private val messageNavigator: MessageActivityNavigator) : CoreNavigator(app) {
    /**
     * Переход к секциям
     */
    fun navigateToSections() {
        addCommands(ToSectionsCommand())
    }
    /**
     * переход к типам замеров
     */
    fun navigateToMeasurementTypes() {
        addCommands(ToMeasurementTypesCommand())
    }
    /**
     * возврат назад к типам замеров
     */
    fun navigateBackToMeasurementTypes() {
        addCommands(BackToMeasurementTypesCommand())
    }
    /**
     * переход к профилометрам
     */
    fun navigateToProfilometerMeasurements(measureId: String) {
        addCommands(ToProfilerMeasurementsCommand(measureId))
    }
    /**
     * переход к деталке замера
     */
    fun navigateToMeasurementDetail(categoryId: String) {
        addCommands(ToMeasurementDetailCommand(categoryId))
    }
    /**
     * переход к причинам обточки КП
     */
    fun navigateToTuningReasons(wheelPairPosition: Int) {
        addCommands(ToTuningReasonsCommand(wheelPairPosition))
    }
    /**
     * переход к колесным парам
     */
    fun navigateToWheelPairs() {
        addCommands(ToMeasurementWheelPairsCommand())
    }
    /**
     * переход к редактированию замера
     */
    fun navigateToEditMeasurement(wheelPairPosition: Int) {
        addCommands(ToEditMeasurementCommand(wheelPairPosition))
    }
    /**
     * переход к подтверждению замера
     */
    fun navigateToMeasurementConfirmation() {
        addCommands(ToMeasurementConfirmationCommand())
    }
    /**
     * переход к отчетам
     */
    fun navigateToReports() {
        addCommands(ToReportsCommand())
    }
    /**
     * переход к телефонной книге
     */
    fun navigateToPhoneBook() {
        addCommands(ToPhoneBookCommand())
    }
    /**
     * переход к сообщениям
     */
    fun navigateToMessage() {
        addCommands(ToMessageCommand())
    }
    /**
     * команды для наигации
     */
    private inner class ToMeasurementConfirmationCommand() : Command {
        override fun apply() {
            /**
             * Интент для подтверждения замеров
             */
            val intent = MeasurementConfirmationActivity.getCallingIntent(currentActivity)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class ToSectionsCommand() : Command {
        override fun apply() {
            /**
             * Интент для секций
             */
            val intent = SectionsActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class ToProfilerMeasurementsCommand(private val measureId: String) : Command {
        override fun apply() {
            /**
             * Интент для профилометров
             */
            val intent = ProfilometersActivity.getCallingIntent(currentActivity, measureId)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class ToMeasurementTypesCommand : Command {
        override fun apply() {
            /**
             * Интент для типов замеров
             */
            val intent = MeasurementTypesActivity.getCallingIntent(currentActivity)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class BackToMeasurementTypesCommand() : Command {
        override fun apply() {
            /**
             * Интент для возврата к тиапм замеров
             */
            val intent = MeasurementTypesActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            currentActivity.startActivity(intent)
            animBack(currentActivity)
        }
    }
    private inner class ToMeasurementDetailCommand(private val categoryId: String) : Command {
        override fun apply() {
            /**
             * Интент для деталки замера
             */
            val intent = MeasurementDetailActivity.getCallingIntent(currentActivity, categoryId)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class ToTuningReasonsCommand(private val wheelPairPosition: Int) : Command {
        override fun apply() {
            /**
             * Интент для причин оботчки
             */
            val intent = TuningReasonsActivity.getCallingIntent(currentActivity, wheelPairPosition)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class ToMeasurementWheelPairsCommand() : Command {
        override fun apply() {
            /**
             * Интент для замеров колесных пар
             */
            val intent = MeasurementWheelPairsActivity.getCallingIntent(currentActivity)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }
    private inner class ToEditMeasurementCommand(private val wheelPairPosition: Int) : Command {
        override fun apply() {
            /**
             * Интент для редактирования замероа
             */
            val intent = EditMeasurementActivity.getCallingIntent(currentActivity, wheelPairPosition)
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

    private inner class ToReportsCommand : Command {
        override fun apply() {
            /**
             * Интент для отчетов
             */
            val intent = ReportsActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

    private inner class ToPhoneBookCommand : Command {
        override fun apply() {
            /**
             * Интент для телефонной книги
             */
            val intent = TelephoneDirectoryActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

    private inner class ToMessageCommand : Command {
        override fun apply() {
            messageNavigator.navigateToMessageActivity(currentActivity)
        }
    }
}