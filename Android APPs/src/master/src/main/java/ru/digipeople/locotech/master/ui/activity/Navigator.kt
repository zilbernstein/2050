package ru.digipeople.locotech.master.ui.activity

import android.app.Activity
import ru.digipeople.locotech.master.ui.activity.comment.CommentParams
import ru.digipeople.locotech.master.ui.activity.divide.DivideParams
import ru.digipeople.locotech.master.ui.activity.measurement.MeasureParams
import ru.digipeople.locotech.master.ui.activity.partlyaccept.PartlyAcceptParams
import ru.digipeople.locotech.master.ui.activity.performance.PerformanceActivity
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerActivity
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerParams
import ru.digipeople.locotech.master.ui.activity.tmcamount.TmcAmountParams
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListParameters
import ru.digipeople.locotech.master.ui.activity.writeofftmcamount.WriteOffTmcAmountParams
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигация для МП Мастер
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
    /**
     * Навигация к добавлению работы
     */
    fun navigateToAddWork(remarkId: String) {
        post {
            activityNavigator.navigateToAllWorkActivity(it, remarkId)
        }
    }
    /**
     * Навигация к добавлниею работы из замечания
     */
    fun navigateToAddWorkFromCustomRemark(remarkId: String) {
        post {
            activityNavigator.navigateToAllWorkAсtivityFromCustomRemark(it, remarkId)
        }
    }
    /**
     * Навигация к комментариям из замечания
     */
    fun navigateToRemarkComment(commentParams: CommentParams) {
        post {
            activityNavigator.navigateToRemarkCommentActivity(it, commentParams)
        }
    }
    /**
     * Навигация к комментариям из работы
     */
    fun navigateToWorkComment(commentParams: CommentParams) {
        post {
            activityNavigator.navigateToWorkCommentActivity(it, commentParams)

        }
    }
    /**
     * Навигация к комментариям
     */
    fun navigateToEditCommentForResult(commentParams: CommentParams) {
        post {
            activityNavigator.navigateToCommentActivityForResult(it, commentParams, PerformanceActivity.EDIT_COMMENT_BEFORE_RETURN)
        }
    }
    /**
     * Навигация к фотогалереи из замечания
     */
    fun navigateToRemarkPhotoGallery(remarkId: String) {
        post {
            activityNavigator.navigateToPhotoGalleryActivity(it, remarkId)
        }
    }
    /**
     * Навигация к фотогалереи из работы
     */
    fun navigateToWorkPhotoGallery(workId: String) {
        post {
            activityNavigator.navigateToWorkPhotoGalleryActivity(it, workId)
        }
    }
    /**
     * Навигация к поиску замечания
     */
    fun navigateToSearchRemark() {
        post {
            activityNavigator.navigateToSearchRemarkActivity(it)
        }
    }
    /**
     * Навигация к добавлнию исполнителя
     */
    fun navigateToAddPerformer(params: SearchPerformerParams) {
        post {
            activityNavigator.navigateToPerformerSearchActivity(it, params, SearchPerformerActivity.CALLING_LOCOMOTIVE_DETAIL)
        }
    }
    /**
     * Навигация к проверке работ перед принятем
     */
    fun navigateToCheckWorkBeforeAccept() {
        post {
            activityNavigator.navigateToCheckWorkActivityForAccept(it)
        }
    }
    /**
     * Навигация к проверке работ перед запуском
     */
    fun navigateToCheckWorkBeforeStart() {
        post {
            activityNavigator.navigateToCheckWorkActivityForStart(it)
        }
    }
    /**
     * Навигация к проверке работ перед согласованием
     */
    fun navigateToCheckWorkBeforeApprove() {
        post {
            activityNavigator.navigateToCheckWorkActivityForApprove(it)
        }
    }
    /**
     * Навигация к списку работ
     */
    fun navigateToWorkList(remarkId: String) {
        post {
            activityNavigator.navigateToWorkListActivity(it, remarkId)
        }
    }
    /**
     * Навигация к замечаниям
     */
    fun navigateToRemark() {
        post {
            activityNavigator.navigateToLocomotiveDetailsActivity(it)
        }
    }
    /**
     * Навигация к списку ТМЦ
     */
    fun navigateToTmcList(params: TmcListParameters?) {
        post {
            activityNavigator.navigateToTmcListActivity(it, params)
        }
    }
    /**
     * Навигация к поиску ТМЦ
     */
    fun navigateToTmcSearch(workId: String, tmcIdList: ArrayList<String>) {
        post {
            activityNavigator.navigateToTmcSearchActivity(it, workId, tmcIdList)
        }
    }
    /**
     * Навигация к разделению работ
     */
    fun navigateToDivide(divideParams: DivideParams) {
        post {
            activityNavigator.navigateToDivideActivity(it, divideParams)
        }
    }
    /**
     * Навигация к частичной приемке работ
     */
    fun navigateToPartlyAccept(partlyAcceptParams: PartlyAcceptParams) {
        post {
            activityNavigator.navigateToPartlyAcceptActivity(it, partlyAcceptParams)
        }
    }
    /**
     * Навигация к установке кол-ва ТМЦ
     */
    fun navigateToTmcAmount(params: TmcAmountParams) {
        post {
            activityNavigator.navigateToTmcAmountActivity(it, params)
        }
    }
    /**
     * Навигация к списанию ТМЦ
     */
    fun navigateToTmcBeforeAccept(workIds: ArrayList<String>) {
        post {
            activityNavigator.navigateToTmcBeforeAccept(it, workIds)
        }
    }
    /**
     * Навигация к списанию ТМЦ
     */
    fun navigateToTmcBeforeAcceptForResult(workIds: ArrayList<String>, requestCode: Int) {
        post {
            activityNavigator.navigateToTmcBeforeAcceptForResult(it, workIds, requestCode)
        }
    }
    /**
     * Навигация к списанию ТМЦ
     */
    fun navigateToWriteOffTmcAmount(params: WriteOffTmcAmountParams) {
        post {
            activityNavigator.navigateToWriteOffTmcAmount(it, params)
        }
    }
    /**
     * Навигация к замерам
     */
    fun navigateToMeasurement(workId: String, workName: String, workStatus: Int) {
        post {
            activityNavigator.navigateToMeasurementActivity(it, workId, workName, workStatus)
        }
    }
    /**
     * Навигация к изменению замера
     */
    fun navigateToMeasurementEdit(params: MeasureParams) {
        post {
            activityNavigator.navigateToMeasurementEdit(it, params)
        }
    }
    /**
     * Навигация к шаблонам исполниетеля
     */
    fun navigateToAssignmentTemplates() {
        post {
            activityNavigator.navigateToAssignmentTemplates(it)
        }
    }
    /**
     * Навигация к комментариям только для чтения
     */
    fun navigateToReadOnlyComment(text: String) {
        post {
            activityNavigator.navigateToReadOnlyComment(it, text)
        }
    }
    /**
     * Навигация к сканеру
     */
    fun navigateToScanActivity(requestCode: Int) {
        post {
            activityNavigator.navigateToScannerActivity(it, requestCode)
        }
    }
    /**
     * Навигация к предыдущему экрану
     */
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
}