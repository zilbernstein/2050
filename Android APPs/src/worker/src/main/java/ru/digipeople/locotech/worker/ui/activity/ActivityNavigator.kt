package ru.digipeople.locotech.worker.ui.activity

import android.app.Activity
import android.content.Intent
import ru.digipeople.locotech.worker.model.WorkStatus
import ru.digipeople.locotech.worker.ui.activity.choosereason.ChooseReasonActivity
import ru.digipeople.locotech.worker.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.measurements.MeasurementsActivity
import ru.digipeople.locotech.worker.ui.activity.measurements.MeasurementsParams
import ru.digipeople.locotech.worker.ui.activity.mytask.MyTaskActivity
import ru.digipeople.locotech.worker.ui.activity.photo.PhotoActivity
import ru.digipeople.locotech.worker.ui.activity.shift.ShiftActivity
import ru.digipeople.locotech.worker.ui.activity.splash.SplashActivity
import ru.digipeople.locotech.worker.ui.activity.task.TaskActivity
import ru.digipeople.locotech.worker.ui.activity.tmcshortage.TmcShortageActivity
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.telephonebook.ui.TelephoneBookNavigator
import ru.digipeople.ui.activity.base.BaseActivityNavigator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс реализующий навигацию к активити МП Сотрудник
 *
 * @author Kashonkov Nikita
 */
@Singleton
class ActivityNavigator @Inject constructor(private val messageNavigator: MessageActivityNavigator,
                                            private val telephoneBookNavigator: TelephoneBookNavigator,
                                            private val appNavigator: AppNavigator) : BaseActivityNavigator() {
    /**
     * переход к экрану авторизации
     */
    fun navigateToAuthActivity(activity: Activity) {
        appNavigator.navigateToAuth()
    }
    /**
     * переход к экрану начала работы
     */
    fun navigateToShiftActivity(activity: Activity) {
        val intent = ShiftActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к экрану моих заданий
     */
    fun navigateToMytaskActivity(activity: Activity) {
        val intent = MyTaskActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к экрану деталки задания
     */
    fun navigateToTaskActivity(activity: Activity, taskId: String) {
        val intent = TaskActivity.getCallingIntent(activity, taskId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к экрану деталки задания из меню
     */
    fun navigateToTaskActivityFromMenu(activity: Activity, taskId: String) {
        val intent = TaskActivity.getCallingIntent(activity, taskId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к экрану причин остановки задания
     */
    fun navigateToPauseWorkReason(activity: Activity, workId: String, requestCode: Int, commentParams: CommentParams) {
        val intent = ChooseReasonActivity.getCallingIntent(activity, workId, commentParams)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * переход к экрану комментариев
     */
    fun navigateToCommentActivity(activity: Activity, commentParams: CommentParams, requestCode: Int) {
        val intent = CommentActivity.getCallingIntent(activity, commentParams)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * переход к экрану комментариев из замеров
     */
    fun navigateToCommentActivityFromMeasures(activity: Activity, commentParams: CommentParams, requestCode: Int, workStatus: WorkStatus) {
        val intent = CommentActivity.getCallingIntentFromMeasure(activity, commentParams, false, workStatus)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * переход к экрану тмц
     */
    fun navigateToTmcShortageActivity(activity: Activity, workId: String, requestCode: Int, commentParams: CommentParams) {
        val intent = TmcShortageActivity.getCallingIntent(activity, workId, commentParams)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * переход к экрану фотогалереи
     */
    fun navigateToPhotoActivityForResult(activity: Activity, workId: String, requestCode: Int) {
        val intent = PhotoActivity.getCallingIntentForResult(activity, workId)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * переход к экрану фотографии
     */
    fun navigateToPhotoActivity(activity: Activity, workId: String) {
        val intent = PhotoActivity.getCallingIntent(activity, workId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к экрану заставки
     */
    fun navigateToSplashActivity(activity: Activity) {
        val intent = SplashActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к экрану сообщений
     */
    fun navigateToMessageActivity(activity: Activity) {
        messageNavigator.navigateToMessageActivity(activity)
    }
    /**
     * переход к экрану телефонной книги
     */
    fun navigateToTelephoneBookActivity(activity: Activity) {
        telephoneBookNavigator.navigateToTelephoneBookActivity(activity, TelephoneBookNavigator.PORTRAIT)
    }
    /**
     * переход к экрану замеров
     */
    fun navigateToMeasureActivity(activity: Activity, workId: String, workName: String, workStatus: WorkStatus) {
        val intent = MeasurementsActivity.getCallingIntent(activity, MeasurementsParams(workId, workName, workStatus))
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
}