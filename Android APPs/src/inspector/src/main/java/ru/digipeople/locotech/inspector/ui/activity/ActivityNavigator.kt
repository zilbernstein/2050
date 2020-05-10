package ru.digipeople.locotech.inspector.ui.activity

import android.app.Activity
import android.content.Intent
import ru.digipeople.locotech.inspector.ui.activity.checklist.CheckListActivity
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentParams
import ru.digipeople.locotech.inspector.ui.activity.controlpoint.ControlPointActivity
import ru.digipeople.locotech.inspector.ui.activity.createremarkphoto.CreateRemarkPhotoActivity
import ru.digipeople.locotech.inspector.ui.activity.declinereason.DeclineReasonActivity
import ru.digipeople.locotech.inspector.ui.activity.equipment.EquipmentActivity
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionActivity
import ru.digipeople.locotech.inspector.ui.activity.measurement.MeasurementActivity
import ru.digipeople.locotech.inspector.ui.activity.print.PrintActivity
import ru.digipeople.locotech.inspector.ui.activity.readonlycomment.ReadOnlyCommentActivity
import ru.digipeople.locotech.inspector.ui.activity.remarksearch.RemarkSearchActivity
import ru.digipeople.locotech.inspector.ui.activity.repairbook.RepairBookActivity
import ru.digipeople.locotech.inspector.ui.activity.signersearch.SignerSearchActivity
import ru.digipeople.locotech.inspector.ui.activity.splash.SplashActivity
import ru.digipeople.locotech.inspector.ui.activity.summary.SummaryActivity
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryActivity
import ru.digipeople.telephonebook.ui.TelephoneBookNavigator
import ru.digipeople.ui.activity.base.BaseActivityNavigator
import javax.inject.Inject

/**
 * Класс реализующий навигацию к активити МП Выпуск на линию
 *
 * @author Kashonkov Nikita
 */
class ActivityNavigator @Inject constructor(private val messageNavigator: MessageActivityNavigator,
                                            private val telephoneBookNavigator: TelephoneBookNavigator,
                                            private val appNavigator: AppNavigator) : BaseActivityNavigator() {

    fun navigateToAuthActivity(activity: Activity) {
        appNavigator.navigateToAuth()
    }

    fun navigateToSplashActivity(activity: Activity) {
        val intent = SplashActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToEquipmentActivity(activity: Activity) {
        val intent = EquipmentActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToInspectionActivity(activity: Activity) {
        val intent = InspectionActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToInspectionActivityWithRzdReamrk(activity: Activity) {
        val intent = InspectionActivity.getCallingIntentForRemarkRzd(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToInspectionActivityWithOtkRemark(activity: Activity) {
        val intent = InspectionActivity.getCallingIntentForRemarkOtk(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToDeclinedReasonActivity(activity: Activity, remarkId: String) {
        val intent = DeclineReasonActivity.getCallingIntent(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToControlPointActivity(activity: Activity, workId: String) {
        val intent = ControlPointActivity.getCallingIntent(activity, workId)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToRemarkSearch(activity: Activity, startTab: Int) {
        val intent = RemarkSearchActivity.getCallingIntent(activity, startTab)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToCommentActivityForResult(activity: Activity, commentParams: CommentParams, requestCode: Int) {
        val intent = CommentActivity.getCallingIntentForResult(activity, commentParams)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }

    fun navigateToWorkCommentActivity(activity: Activity, commentParams: CommentParams) {
        val intent = CommentActivity.getCallingIntentForWorkComment(activity, commentParams)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToRemarkCommentActivity(activity: Activity, commentParams: CommentParams, requestCode: Int, startTab: Int) {
        val intent = CommentActivity.getCallingIntentForRemarkComment(activity, commentParams, startTab)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }

    fun navigateToCsoCommentActivity(activity: Activity, commentParams: CommentParams) {
        val intent = CommentActivity.getCallingIntentForRemarkComment(activity, commentParams, 0)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToRepairBookActivity(activity: Activity) {
        val intent = RepairBookActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToCheckListActivity(activity: Activity) {
        val intent = CheckListActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToSummaryActivity(activity: Activity) {
        val intent = SummaryActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToPrintActivity(activity: Activity) {
        val intent = PrintActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToSearchSignerActivity(activity: Activity) {
        val intent = SignerSearchActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToPhotoGalleryActivity(activity: Activity, remarkId: String) {
        val intent = PhotoGalleryActivity.getCallingIntentForRemark(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToCreateRemarkPhotoActivity(activity: Activity, remarkId: String, requestCode: Int, startTab: Int) {
        val intent = CreateRemarkPhotoActivity.getCallingIntent(activity, remarkId, startTab)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }

    fun navigateToWorkPhotoGalleryActivity(activity: Activity, workId: String) {
        val intent = PhotoGalleryActivity.getCallingIntentForWork(activity, workId)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToCsoPhotoGalleryActivity(activity: Activity, workId: String) {
        val intent = PhotoGalleryActivity.getCallingIntentForWork(activity, workId)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToPhoneBookActivity(activity: Activity) {
        telephoneBookNavigator.navigateToTelephoneBookActivity(activity, TelephoneBookNavigator.LANDSCAPE)
    }

    fun navigateToMessageActivity(activity: Activity) {
        messageNavigator.navigateToMessageActivity(activity)
    }

    fun navigateToReadOnlyComment(activity: Activity, text: String) {
        val intent = ReadOnlyCommentActivity.getCallingIntent(activity, text)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToMeasurementActivity(activity: Activity, workId: String, workName: String, workStatus: Int) {
        val intent = MeasurementActivity.getCallingIntent(activity, workId, workName, workStatus)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }

}