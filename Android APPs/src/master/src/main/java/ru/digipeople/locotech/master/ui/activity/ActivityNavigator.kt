package ru.digipeople.locotech.master.ui.activity

import android.app.Activity
import android.content.Intent
import ru.digipeople.locotech.core.ui.activity.auth.AuthActivity
import ru.digipeople.locotech.master.ui.activity.allworklist.AllWorkActivity
import ru.digipeople.locotech.master.ui.activity.approved.ApprovedActivity
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.AssignmentTemplatesActivity
import ru.digipeople.locotech.master.ui.activity.checkwork.CheckWorkActivity
import ru.digipeople.locotech.master.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.master.ui.activity.comment.CommentParams
import ru.digipeople.locotech.master.ui.activity.divide.DivideActivity
import ru.digipeople.locotech.master.ui.activity.divide.DivideParams
import ru.digipeople.locotech.master.ui.activity.groupassignment.GroupAssignmentActivity
import ru.digipeople.locotech.master.ui.activity.measurement.MeasureParams
import ru.digipeople.locotech.master.ui.activity.measurement.MeasurementActivity
import ru.digipeople.locotech.master.ui.activity.measurementedit.MeasurementEditActivity
import ru.digipeople.locotech.master.ui.activity.partlyaccept.PartlyAcceptActivity
import ru.digipeople.locotech.master.ui.activity.partlyaccept.PartlyAcceptParams
import ru.digipeople.locotech.master.ui.activity.readonlycomment.ReadOnlyCommentActivity
import ru.digipeople.locotech.master.ui.activity.remark.RemarkActivity
import ru.digipeople.locotech.master.ui.activity.remarksearch.RemarkSearchActivity
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerActivity
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerParams
import ru.digipeople.locotech.master.ui.activity.settings.SettingsActivity
import ru.digipeople.locotech.master.ui.activity.splash.SplashActivity
import ru.digipeople.locotech.master.ui.activity.status.StatusActivity
import ru.digipeople.locotech.master.ui.activity.tmcamount.TmcAmountActivity
import ru.digipeople.locotech.master.ui.activity.tmcamount.TmcAmountParams
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.TMCBeforeAcceptActivity
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListActivity
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListParameters
import ru.digipeople.locotech.master.ui.activity.tmcsearch.TmcSearchActivity
import ru.digipeople.locotech.master.ui.activity.urgent.UrgentActivity
import ru.digipeople.locotech.master.ui.activity.workerspresence.WorkersPresenceActivity
import ru.digipeople.locotech.master.ui.activity.worklist.WorkListActivity
import ru.digipeople.locotech.master.ui.activity.writeofftmcamount.WriteOffTmcAmountActivity
import ru.digipeople.locotech.master.ui.activity.writeofftmcamount.WriteOffTmcAmountParams
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryActivity
import ru.digipeople.qrscanner.ui.activity.QrScannerNavigator
import ru.digipeople.telephonebook.ui.activity.telephone.TelephoneBookActivity
import ru.digipeople.ui.activity.base.BaseActivityNavigator
import javax.inject.Inject

/**
 * Навигатор для активити
 *
 * @author Kashonkov Nikita
 */
class ActivityNavigator @Inject constructor(private val messageNavigator: MessageActivityNavigator,
                                            private val appNavigator: AppNavigator,
                                            private val scanNavigator: QrScannerNavigator) : BaseActivityNavigator() {
    /**
     * Навигация к экрану срочно
     */
    fun navigateToUrgentActivity(activity: Activity) {
        val intent = UrgentActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану заставке
     */
    fun navigateToSplashActivity(activity: Activity) {
        val intent = SplashActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    /**
     * Навигация к экрану авторизации
     */
    fun navigateToAuthActivity(activity: Activity) {
        val intent = AuthActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану сообщений
     */
    fun navigateToMessageActivity(activity: Activity) {
        messageNavigator.navigateToMessageActivity(activity)
    }
    /**
     * Навигация к экрану настроек
     */
    fun navigateToSettingsActivity(activity: Activity) {
        val intent = SettingsActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToStatusActivity(activity: Activity) {
        val intent = StatusActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану согласования
     */
    fun navigateToApprovedActivity(activity: Activity) {
        val intent = ApprovedActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану группового назначения исполнителей
     */
    fun navigateToAssignmentActivity(activity: Activity) {
        val intent = GroupAssignmentActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану явка сотрудников
     */
    fun navigateToWorkersPresenceActivity(activity: Activity) {
        val intent = WorkersPresenceActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану замечаний
     */
    fun navigateToLocomotiveDetailsActivity(activity: Activity) {
        val intent = RemarkActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану телефонной книги
     */
    fun navigateToPhoneBookActivity(activity: Activity) {
        val intent = TelephoneBookActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану посика замечания
     */
    fun navigateToSearchRemarkActivity(activity: Activity) {
        val intent = RemarkSearchActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану добавления замечаний - работ
     */
    fun navigateToWorkListActivity(activity: Activity, remarkId: String) {
        val intent = WorkListActivity.getCallingIntent(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану поиска исполнителя
     */
    fun navigateToPerformerSearchActivity(activity: Activity, params: SearchPerformerParams, callingId: Int) {
        val intent = SearchPerformerActivity.getCallingIntent(activity, params, callingId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану проверки выбранных работ
     */
    fun navigateToCheckWorkActivityForStart(activity: Activity) {
        val intent = CheckWorkActivity.getCallingIntentForStart(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану проверки выбранных работ для согласования
     */
    fun navigateToCheckWorkActivityForApprove(activity: Activity) {
        val intent = CheckWorkActivity.getCallingIntentForApprove(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану проверки выбранных работ для принятия
     */
    fun navigateToCheckWorkActivityForAccept(activity: Activity) {
        val intent = CheckWorkActivity.getCallingIntentForAccept(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к списку работ с сощдания замечания
     */
    fun navigateToAllWorkAсtivityFromCustomRemark(activity: Activity, remarkId: String) {
        val intent = AllWorkActivity.getCallingIntentForCustomRemark(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к спсику работ
     */
    fun navigateToAllWorkActivity(activity: Activity, remarkId: String) {
        val intent = AllWorkActivity.getCallingIntent(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к комментариям
     */
    fun navigateToCommentActivityForResult(activity: Activity, commentParams: CommentParams, requestCode: Int) {
        val intent = CommentActivity.getCallingIntentForResult(activity, commentParams)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * Навигация к экрану списания тмц перед приемом работы
     */
    fun navigateToTmcBeforeAcceptForResult(activity: Activity, workIds: ArrayList<String>, requestCode: Int) {
        val intent = TMCBeforeAcceptActivity.getCallingIntent(activity, workIds)
        activity.startActivityForResult(intent, requestCode)
        animForward(activity)
    }
    /**
     * Навигация к экрану комментариев с работы
     */
    fun navigateToWorkCommentActivity(activity: Activity, commentParams: CommentParams) {
        val intent = CommentActivity.getCallingIntentForWorkComment(activity, commentParams)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану комментариев с замечаний
     */
    fun navigateToRemarkCommentActivity(activity: Activity, commentParams: CommentParams) {
        val intent = CommentActivity.getCallingIntentForRemarkComment(activity, commentParams)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к фотогалерее
     */
    fun navigateToPhotoGalleryActivity(activity: Activity, remarkId: String) {
        val intent = PhotoGalleryActivity.getCallingIntentForRemark(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }

    fun navigateToWorkPhotoGalleryActivity(activity: Activity, workId: String) {
        val intent = PhotoGalleryActivity.getCallingIntentForWork(activity, workId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к списку ТМЦ
     */
    fun navigateToTmcListActivity(activity: Activity, params: TmcListParameters?) {
        val intent = TmcListActivity.getCallingIntent(activity, params)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану поиска ТМЦ
     */
    fun navigateToTmcSearchActivity(activity: Activity, workId: String, tmcIdList: ArrayList<String>) {
        val intent = TmcSearchActivity.getCallingIntent(activity, workId, tmcIdList)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану ввода количества ТМЦ
     */
    fun navigateToTmcAmountActivity(activity: Activity, params: TmcAmountParams) {
        val intent = TmcAmountActivity.getCallingIntent(activity, params)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к списанию ТМЦ
     */
    fun navigateToTmcBeforeAccept(activity: Activity, workIds: ArrayList<String>) {
        val intent = TMCBeforeAcceptActivity.getCallingIntent(activity, workIds)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к списанию ТМЦ
     */
    fun navigateToWriteOffTmcAmount(activity: Activity, params: WriteOffTmcAmountParams) {
        val intent = WriteOffTmcAmountActivity.getCallingIntent(activity, params)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану разделения работ
     */
    fun navigateToDivideActivity(activity: Activity, params: DivideParams) {
        val intent = DivideActivity.newInstance(activity, params)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану частчичной приемки работы
     */
    fun navigateToPartlyAcceptActivity(activity: Activity, params: PartlyAcceptParams) {
        val intent = PartlyAcceptActivity.getCallingIntent(activity, params)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану замеров
     */
    fun navigateToMeasurementActivity(activity: Activity, workId: String, workName: String, workStatus: Int) {
        val intent = MeasurementActivity.getCallingIntent(activity, workId, workName, workStatus)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к редактированию замеров
     */
    fun navigateToMeasurementEdit(activity: Activity, params: MeasureParams) {
        val intent = MeasurementEditActivity.getCallingIntent(activity, params)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к комментариям только для чтения
     */
    fun navigateToReadOnlyComment(activity: Activity, text: String) {
        val intent = ReadOnlyCommentActivity.getCallingIntent(activity, text)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану выбора шаблона исполнителей
     */
    fun navigateToAssignmentTemplates(activity: Activity) {
        val intent = AssignmentTemplatesActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Навигация к экрану сканера
     */
    fun navigateToScannerActivity(activity: Activity, requestCode: Int) {
        scanNavigator.navigateToScannerActivity(activity, requestCode)
    }
}