package ru.digipeople.locotech.worker.ui.activity

import android.app.Activity
import ru.digipeople.locotech.worker.model.WorkStatus
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.measurements.MeasureParams
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигация для МП Сотрудник
 *
 * @author Kashonkov Nikita
 */
@Singleton
class Navigator @Inject constructor(private val activityNavigator: ActivityNavigator) {
    //region Other
    private var activity: Activity? = null
    //end Region

    internal fun onResume(activity: Activity) {
        this.activity = activity
    }

    internal fun onPause() {
        this.activity = null
    }
    /**
     * переход к экрану деталки задания
     */
    fun navigateToWork(taskId: String) {
        if (activity != null) {
            activityNavigator.navigateToTaskActivity(activity!!, taskId)
        }
    }
    /**
     * переход к экрану причин остановки задания
     */
    fun navigateToPauseWork(workId: String, requestCode: Int, commentParams: CommentParams) {
        if (activity != null) {
            activityNavigator.navigateToPauseWorkReason(activity!!, workId, requestCode, commentParams)
        }
    }
    /**
     * переход к экрану списка заданий
     */
    fun navigateToMyTask() {
        if (activity != null) {
            activityNavigator.navigateToMytaskActivity(activity!!)
        }
    }
    /**
     * переход к экрану комментариев
     */
    fun navigateToComment(commentParams: CommentParams, requestId: Int) {
        if (activity != null) {
            activityNavigator.navigateToCommentActivity(activity!!, commentParams, requestId)
        }
    }
    /**
     * переход к экрану комментариев из замеров
     */
    fun navigateToCommentFromMeasures(commentParams: CommentParams, requestId: Int, workStatus: WorkStatus) {
        if (activity != null) {
            activityNavigator.navigateToCommentActivityFromMeasures(activity!!, commentParams, requestId, workStatus)
        }
    }
    /**
     * переход к экрану выбора тмц
     */
    fun navigateToChooseTmc(taskId: String, requestId: Int, commentParams: CommentParams) {
        if (activity != null) {
            activityNavigator.navigateToTmcShortageActivity(activity!!, taskId, requestId, commentParams)
        }
    }
    /**
     * переход к экрану старта работ
     */
    fun navigateToShiftActivity() {
        if (activity != null) {
            activityNavigator.navigateToShiftActivity(activity!!)
        }
    }
    /**
     * переход к экрану фотографии
     */
    fun navigateToPhotoForResult(taskId: String, requestId: Int) {
        if (activity != null) {
            activityNavigator.navigateToPhotoActivityForResult(activity!!, taskId, requestId)
        }
    }
    /**
     * переход к экрану фото
     */
    fun navigateToPhoto(taskId: String) {
        if (activity != null) {
            activityNavigator.navigateToPhotoActivity(activity!!, taskId)
        }
    }
    /**
     * переход к экрану заставки
     */
    fun navigateToSplash() {
        if (activity != null) {
            activityNavigator.navigateToSplashActivity(activity!!)
        }
    }
    /**
     * переход к предыдущему экрану
     */
    fun navigateBack() {
        if (activity != null) {
            activityNavigator.navigateBack(activity!!)
        }
    }
    /**
     * переход к экрану замеров
     */
    fun navigateToMeasure(workId: String, workName: String, workStatus: WorkStatus) {
        if (activity != null) {
            activityNavigator.navigateToMeasureActivity(activity!!, workId, workName, workStatus)
        }
    }
}