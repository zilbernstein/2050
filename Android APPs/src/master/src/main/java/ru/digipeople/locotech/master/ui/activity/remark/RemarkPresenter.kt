package ru.digipeople.locotech.master.ui.activity.remark

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.master.interactor.SetWorkaroundInteractor
import ru.digipeople.locotech.master.model.Remark
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.comment.CommentParams
import ru.digipeople.locotech.master.ui.activity.remark.interactor.RemarkLoader
import ru.digipeople.locotech.master.ui.activity.remark.interactor.RemarkWorker
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListParameters
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер замечаний
 *
 * @author Kashonkov Nikita
 */
class RemarkPresenter @Inject constructor(
        viewState: RemarkViewState,
        private val subscriptionProvider: SubscriptionProvider,
        private val navigator: Navigator,
        private val remarkWorker: RemarkWorker,
        private val remarkLoader: RemarkLoader,
        private val setWorkaroundInteractor: SetWorkaroundInteractor
) : BaseMvpViewStatePresenter<RemarkView, RemarkViewState>(viewState) {
    //region Other
    private var loadDisposable = Disposables.disposed()
    private var workerDisposable = Disposables.disposed()
    private var remarkDisposable = Disposables.disposed()
    private var workDisposable = Disposables.disposed()
    private var workStatusDisposable = Disposables.disposed()
    //endRegion
    private var isFirstLoading = true
    private lateinit var deletingWork: Work
    private lateinit var workList: List<Work>
    private lateinit var remarkList: List<Remark>
    private var currentRemark: Remark? = null
    /**
     * Инициализация
     */
    override fun onInitialize() {}

    fun onScreenShown() {
        loadData(!isFirstLoading)
        isFirstLoading = false
        subscribeToRemarkAndWork()
    }
    /**
     * Обработка нажатия на замечание
     */
    fun onRemarkItemClicked(remark: Remark) {
        workList = remark.workList
        currentRemark = remark
        view.setDataToWorkAdapter(workList, false)
    }
    /**
     * Обрабокта создания замечания
     */
    fun createRemarkClicked() {
        navigator.navigateToSearchRemark()
    }
    /**
     * удаление замечания
     */
    fun deleteRemarkClicked(remark: Remark) {
        deleteRemark(remark)
    }
    /**
     * Удаление работы
     */
    fun deleteWorkItemClicked(work: Work) {
        deletingWork = work
        view.showDeleteDialog()

    }
    /**
     * Обработка согласия на удаление работы
     */
    fun onDeleteWorkAgreeBtnClicked() {
        deleteWork(deletingWork)
    }
    /**
     * Обработка нажатия на ТМЦ
     */
    fun tmcClicked(work: Work) {
        showTmc(work)
    }
    /**
     * Обработка создания работы
     */
    fun createWorkClicked() {
        if (currentRemark != null) {
            navigator.navigateToAddWork(currentRemark!!.id)
        }
    }
    /**
     * Просмотр комментария к работе
     */
    fun showWorkComment(work: Work) {
        val commentParams = CommentParams(work.id, work.title, work.comment?.text)
        navigator.navigateToWorkComment(commentParams)
    }
    /**
     * Обработка нажатия на комментарий
     */
    fun onCommentClicked(remark: Remark) {
        val commentParams = CommentParams(remark.id, remark.title, remark.comment?.text)
        navigator.navigateToRemarkComment(commentParams)
    }

    fun onPhotoClicked(remark: Remark) {
        navigator.navigateToRemarkPhotoGallery(remark.id)
    }
    /**
     * Обработка обходного решения
     */
    fun onWorkaroundClicked(work: Work) {
        if (WorkStatus.lessThen(work.status, WorkStatus.IN_TASK)) {
            workStatusDisposable = setWorkaroundInteractor.setWorkaround(work.id)
                    .subscribeOn(AppSchedulers.io())
                    .observeOn(AppSchedulers.ui())
                    .subscribe({ result ->
                        /**
                         * обработка результата
                         */
                        if (result.isSuccessful) {
                            loadData(true)
                        } else {
                            view.showError(result.userError.message)
                        }
                    }, {
                        /**
                         * отображение ошибки
                         */
                        view.showError(it.message!!)
                    })
        }
    }
    /**
     * Действия при уничтожении тулбара
     */
    override fun destroy() {
        loadDisposable.dispose()
        workerDisposable.dispose()
        remarkDisposable.dispose()
        workDisposable.dispose()
        workStatusDisposable.dispose()
        super.destroy()
    }
    /**
     * Загрузка данных
     */
    private fun loadData(shouldSavePosition: Boolean = false) {
        loadDisposable.dispose()
        loadDisposable = remarkLoader.loadRemark()
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful && result.remarks!!.size > 0) {
                        remarkList = result.remarks
                        if (currentRemark == null) {
                            currentRemark = remarkList.get(0)
                        } else {
                            val oldId = currentRemark!!.id
                            currentRemark = null
                            remarkList.forEach {
                                if (it.id.equals(oldId, true)) {
                                    currentRemark = it
                                    return@forEach
                                }
                            }
                            if (currentRemark == null) {
                                currentRemark = remarkList.get(0)
                            }
                        }
                    }
                }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.setLoadingVisible(false)
                            if (result.isSuccessful) {
                                if (result.remarks!!.isNotEmpty()) {
                                    view.setDataVisibility(true)
                                    view.setDataToRemarkAdapter(currentRemark!!, remarkList, shouldSavePosition)
                                    view.setDataToWorkAdapter(currentRemark!!.workList, shouldSavePosition)
                                } else {
                                    view.setDataVisibility(false)
                                }
                                /**
                                 * Установка прогресса
                                 */
                                view.setEquipmentProgress(result.equipmentProgress, result.leftTime, result.requiredTime)
                            } else {
                                /**
                                 * Сообщение об ошибке
                                 */
                                view.setDataVisibility(false)
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Удаление работы
     */
    private fun deleteWork(work: Work) {
        workerDisposable.dispose()
        workerDisposable = remarkWorker.revokeWork(work.id)
                .subscribeOn(AppSchedulers.io())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AppSchedulers.ui())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        loadData()
                    } else {
                        /**
                         * Сообщение об ошибке
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Удаление замечания
     */
    private fun deleteRemark(remark: Remark) {
        workerDisposable.dispose()
        workerDisposable = remarkWorker.revokeRemark(remark.id)
                .subscribeOn(AppSchedulers.io())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        loadData()
                    } else {
                        /**
                         * Сообщение об ошибке
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Переход к ТМЦ
     */
    private fun showTmc(work: Work) {
        navigator.navigateToTmcList(TmcListParameters(work.id, work.title))
    }
    /**
     * Подписка на замечания и работы
     */
    private fun subscribeToRemarkAndWork() {
        remarkDisposable.dispose()
        remarkDisposable = subscriptionProvider.remarkSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadData(true) }
        workerDisposable.dispose()
        workerDisposable = subscriptionProvider.workSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadData(true) }
    }
}