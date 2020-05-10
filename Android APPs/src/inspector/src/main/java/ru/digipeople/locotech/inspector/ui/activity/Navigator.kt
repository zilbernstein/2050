package ru.digipeople.locotech.inspector.ui.activity

import android.app.Activity
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentParams
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигация для МП Выпуск на линию
 *
 * @author Kashonkov Nikita
 */
@Singleton
class Navigator @Inject constructor(private val activityNavigator: ActivityNavigator) {
    //region Other
    private var activity: Activity? = null
    private val buffer = LinkedList<(activity: Activity) -> Unit>()
    //end Region

    internal fun onResume(activity: Activity) {
        this.activity = activity
        while (buffer.isNotEmpty()) {
            buffer.pop()(activity)
        }
    }

    internal fun onPause() {
        this.activity = null
    }

    fun navigateToAuth() {
        post {
            activityNavigator.navigateToAuthActivity(it)
        }
    }

    fun navigateToSplash() {
        post {
            activityNavigator.navigateToSplashActivity(it)
        }
    }

    fun navigateToInspectionActivity() {
        post {
            activityNavigator.navigateToInspectionActivity(it)
        }
    }

    fun navigateToInspectionActivityWithRzdRemark() {
        post {
            activityNavigator.navigateToInspectionActivityWithRzdReamrk(it)
        }
    }

    fun navigateToInspectionActivityWithOtkRemark() {
        post {
            activityNavigator.navigateToInspectionActivityWithOtkRemark(it)
        }
    }


    fun navigateToControlPointActivity(workId: String) {
        post {
            activityNavigator.navigateToControlPointActivity(it, workId)
        }
    }

    fun navigateToEquipment() {
        post {
            activityNavigator.navigateToEquipmentActivity(it)
        }
    }

    fun navigateToRemarkSearch(startTab: Int) {
        post {
            activityNavigator.navigateToRemarkSearch(it, startTab)
        }
    }

    fun navigateToDeclinedReason(remarkId: String) {
        post {
            activityNavigator.navigateToDeclinedReasonActivity(it, remarkId)
        }
    }

    fun navigateToRepairBook() {
        post {
            activityNavigator.navigateToRepairBookActivity(it)
        }
    }

    fun navigateToCheckList() {
        post {
            activityNavigator.navigateToCheckListActivity(it)
        }
    }

    fun navigateToSummary() {
        post {
            activityNavigator.navigateToSummaryActivity(it)
        }
    }

    fun navigateToSignerSearch() {
        post {
            activityNavigator.navigateToSearchSignerActivity(it)
        }
    }

    fun navigateToPrint() {
        post {
            activityNavigator.navigateToPrintActivity(it)
        }
    }

    fun navigateToRemarkPhotoGallery(remarkId: String) {
        post {
            activityNavigator.navigateToPhotoGalleryActivity(it, remarkId)
        }
    }

    fun navigateToCreateRemarkPhoto(remarkId: String, requestCode: Int, startTab: Int) {
        post {
            activityNavigator.navigateToCreateRemarkPhotoActivity(it, remarkId, requestCode, startTab)
        }
    }

    fun navigateToWorkPhotoGallery(workId: String) {
        post {
            activityNavigator.navigateToWorkPhotoGalleryActivity(it, workId)
        }
    }

    fun navigateToCsoPhotoGallery(workId: String) {
        post {
            activityNavigator.navigateToCsoPhotoGalleryActivity(it, workId)
        }
    }

    fun navigateToRemarkComment(commentParams: CommentParams, requestCode: Int, startTab: Int) {
        post {
            activityNavigator.navigateToRemarkCommentActivity(it, commentParams, requestCode, startTab)
        }
    }

    fun navigateToCsoComment(commentParams: CommentParams) {
        post {
            activityNavigator.navigateToCsoCommentActivity(it, commentParams)
        }
    }

    fun navigateToWorkComment(commentParams: CommentParams) {
        post {
            activityNavigator.navigateToWorkCommentActivity(it, commentParams)
        }
    }

    fun navigateBack() {
        post {
            activityNavigator.navigateBack(it)
        }
    }

    fun post(func: (activity: Activity) -> Unit) {
        if (activity != null) {
            func(activity!!)
        } else {
            buffer.addLast(func)
        }
    }

    fun navigateToReadOnlyComment(text: String) {
        post {
            activityNavigator.navigateToReadOnlyComment(it, text)
        }
    }

    fun navigateToMeasurement(workId: String, workName: String, workStatus: Int) {
        post {
            activityNavigator.navigateToMeasurementActivity(it, workId, workName, workStatus)
        }
    }
}