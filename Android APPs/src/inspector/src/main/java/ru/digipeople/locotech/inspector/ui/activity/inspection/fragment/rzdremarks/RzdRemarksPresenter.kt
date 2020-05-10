package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.model.Remark
import ru.digipeople.locotech.inspector.model.UserRole
import ru.digipeople.locotech.inspector.model.Work
import ru.digipeople.locotech.inspector.model.WorkStatus
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentParams
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionFilterState
import ru.digipeople.locotech.inspector.ui.activity.inspection.StartTab
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.RemarkData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.WorkData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.interactor.RemarkRzdLoader
import ru.digipeople.locotech.inspector.ui.fragment.intercator.WorkWorker
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
class RzdRemarksPresenter @Inject constructor(
        viewState: RzdRemarksViewState,
        private val navigator: Navigator,
        private val sessionManager: SessionManager,
        private val remarkLoader: RemarkRzdLoader,
        private val worker: WorkWorker
) : BaseMvpViewStatePresenter<RzdRemarksView, RzdRemarksViewState>(viewState) {
    private var data: List<Remark> = emptyList()
    //region Disposable
    private var filterDisposable = Disposables.disposed()
    private var loadDisposable = Disposables.disposed()
    private var convertDisposable = Disposables.disposed()
    private var workDisposables = Disposables.disposed()
    //end Region
    //region Other
    private var logger = LoggerFactory.getLogger(RzdRemarksPresenter::class)
    private var filter = InspectionFilterState.FILTER_ALL
    //end Region

    /**
     * инициализация
     */
    override fun onInitialize() {
    }

    fun onScreenShown() {
        val signInInfo = sessionManager.requireSignInInfo
        val isRoleRzd = signInInfo.role == UserRole.RZD
        view.setCreateButtonVisibility(isRoleRzd)
        getRemarks()
    }
    /**
     * переход к поиску замечаний
     */
    fun onCreateRemarkClicked() {
        navigator.navigateToRemarkSearch(StartTab.REMARK_RZD.code)
    }
    /**
     * переход к фото по работе
     */
    fun onWorkPhotoClicked(work: Work) {
        navigator.navigateToWorkPhotoGallery(work.id)
    }
    /**
     * переход к фото по замечанию
     */
    fun onRemarkPhotoClciked(remark: Remark) {
        navigator.navigateToRemarkPhotoGallery(remark.id)
    }
    /**
     * принять все
     */
    fun onAcceptAllClicked(remark: RemarkData, remarkPosition: Int) {
        val signInInfo = sessionManager.requireSignInInfo
        when (signInInfo.role) {
            UserRole.SLD -> acceptGroupSld(remark, remarkPosition)
            UserRole.RZD -> acceptGroupRzd(remark, remarkPosition)
        }
    }
    /**
     * принять отк нажатие
     */
    fun onCheckOtkWorkClicked(workData: WorkData, workPosition: Int) {
        val signInInfo = sessionManager.requireSignInInfo
        val isRoleSld = signInInfo.role == UserRole.SLD
        if (!isRoleSld) return
        if (workData.work.status == WorkStatus.ACCEPTED_SLD) {
            declineSld(workData, workPosition)
        } else {
            acceptSld(workData, workPosition)
        }
    }
    /**
     * принять ржд нажатие
     */
    fun onCheckRzdWorkClicked(workData: WorkData, workPosition: Int) {
        val signInInfo = sessionManager.requireSignInInfo
        val isRoleRzd = signInInfo.role == UserRole.RZD
        if (!isRoleRzd) return
        if (workData.work.status == WorkStatus.ACCEPTED_RZD) {
            declineRzd(workData, workPosition)
        } else {
            acceptRzd(workData, workPosition)
        }
    }
    /**
     * нажатие на комментарий работы
     */
    fun onWorkCommentCLicked(work: Work) {
        navigator.navigateToWorkComment(CommentParams(work.id, work.title, work.comment.text))
    }
    /**
     * нажатие на комментарий замечания
     */
    fun onRemarkCommentCLicked(remark: Remark) {
        navigator.navigateToRemarkComment(CommentParams(remark.id, remark.title, remark.comment.text), 0, StartTab.REMARK_RZD.code)
    }
    /**
     * переключение фильтра
     */
    fun onFilterChange(filterStatus: InspectionFilterState) {
        filter = filterStatus
        filterDisposable.dispose()
        filterDisposable = filterData(filter, data)
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    convertAndSetData(data)
                },
                        {
                            view.setLoadingVisibility(false)
                            logger.error(it)
                        })
    }
    /**
     * удаление замечания
     */
    fun onRemarkDeleteClicked(remark: Remark) {
        navigator.navigateToDeclinedReason(remark.title)
    }
    /**
     * Принять группу замечаний отк
     */
    private fun acceptGroupSld(remarkData: RemarkData, groupPosition: Int) {
        workDisposables.dispose()
        var count = 0
        workDisposables = Single.fromCallable {
            val workIds = mutableListOf<String>()
            remarkData.remark.works.forEach {
                if (it.status == WorkStatus.ACCEPTED_MASTER) {
                    workIds.add(it.id)
                    count++
                }
            }
            workIds
        }
                .flatMap { worker.acceptAllSld(it) }
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        remarkData.sldUnCheckCount = remarkData.sldUnCheckCount - count
                        remarkData.isAcceptBtnEnable = false
                        remarkData.remark.works.forEach {
                            if (it.status == WorkStatus.ACCEPTED_MASTER) {
                                it.status = WorkStatus.ACCEPTED_SLD
                            }
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.updateRemark(groupPosition)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * принять отк
     */
    private fun acceptSld(workData: WorkData, workPosition: Int) {
        workDisposables.dispose()
        val list = arrayListOf<String>()
        list.add(workData.work.id)
        workDisposables = worker.acceptAllSld(list)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_SLD
                        workData.remarkData.sldUnCheckCount--
                        workData.remarkData.isAcceptBtnEnable = (workData.remarkData.sldUnCheckCount - workData.remarkData.masterUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * отклонение ОТК
     */
    private fun declineSld(workData: WorkData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.declineSld(workData.work.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_MASTER
                        workData.remarkData.sldUnCheckCount++
                        workData.remarkData.isAcceptBtnEnable = (workData.remarkData.sldUnCheckCount - workData.remarkData.masterUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * принять группу замечаний РЖД
     */
    private fun acceptGroupRzd(remarkData: RemarkData, groupPosition: Int) {
        workDisposables.dispose()
        var count = 0
        workDisposables = Single.fromCallable {
            val workIds = mutableListOf<String>()
            remarkData.remark.works.forEach {
                if (it.status == WorkStatus.ACCEPTED_SLD) {
                    workIds.add(it.id)
                    count++
                }
            }
            workIds
        }
                .flatMap { worker.acceptAllRzd(it) }
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        remarkData.rzdUnCheckCount = remarkData.rzdUnCheckCount - count
                        remarkData.isAcceptBtnEnable = false
                        remarkData.remark.works.forEach {
                            if (it.status == WorkStatus.ACCEPTED_SLD) {
                                it.status = WorkStatus.ACCEPTED_RZD
                            }
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.updateRemark(groupPosition)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Принять РЖД
     */
    private fun acceptRzd(workData: WorkData, workPosition: Int) {
        workDisposables.dispose()
        val list = arrayListOf<String>()
        list.add(workData.work.id)
        workDisposables = worker.acceptAllRzd(list)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_RZD
                        workData.remarkData.rzdUnCheckCount--
                        workData.remarkData.isAcceptBtnEnable = (workData.remarkData.rzdUnCheckCount - workData.remarkData.sldUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * отконение РЖД
     */
    private fun declineRzd(workData: WorkData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.declineRzd(workData.work.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_SLD
                        workData.remarkData.rzdUnCheckCount++
                        workData.remarkData.isAcceptBtnEnable = (workData.remarkData.rzdUnCheckCount - workData.remarkData.sldUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * получить список замечаний
     */
    private fun getRemarks() {
        loadDisposable.dispose()
        loadDisposable = remarkLoader.getRemarks()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        data = result.info!!.remarkList
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }
                .observeOn(Schedulers.computation())
                .flatMap {
                    filterData(filter, data)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    convertAndSetData(result)
                }, {
                    /**
                     * обработка ошибки
                     */
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * фильтрация данных
     */
    private fun filterData(filterStatus: InspectionFilterState, data: List<Remark>): Single<List<Remark>> {
        return when (filterStatus) {
            InspectionFilterState.FILTER_ACTIVE -> {
                Single.fromCallable {
                    val activeData = mutableListOf<Remark>()
                    data.forEach { remark ->
                        /**
                         * обработка результата
                         */
                        val activeRemarkWorkList = remark.works.filter { work ->
                            val signInInfo = sessionManager.requireSignInInfo
                            val isRoleSld = signInInfo.role == UserRole.SLD
                            if (isRoleSld) {
                                work.status == WorkStatus.ACCEPTED_MASTER || work.status == WorkStatus.ACCEPTED_SLD
                            } else {
                                work.status == WorkStatus.ACCEPTED_SLD || work.status == WorkStatus.ACCEPTED_RZD
                            }
                        }
                        if (activeRemarkWorkList.isNotEmpty()) {
                            activeData.add(Remark().apply {
                                id = remark.id
                                works = activeRemarkWorkList
                                title = remark.title
                                author = remark.author
                                date = remark.date
                                photoAmount = remark.photoAmount
                                comment = remark.comment
                                workCount = remark.workCount
                            })
                        }
                    }
                    activeData
                }
            }
            else -> {
                Single.fromCallable { data }
            }
        }
    }
    /**
     * Преобразование и установка данных
     */
    private fun convertAndSetData(list: List<Remark>) {
        convertDisposable.dispose()
        convertDisposable = Single.fromCallable {
            val data = AdapterData()
            var workCount = 0

            val signInInfo = sessionManager.requireSignInInfo
            val isRoleRzd = signInInfo.role == UserRole.RZD
            val isRoleSld = signInInfo.role == UserRole.SLD

            list.forEach { remark ->
                val masterAcceptedList = mutableListOf<Work>()
                val rzdAcceptedList = mutableListOf<Work>()
                val sldAcceptedList = mutableListOf<Work>()

                val remarkData = RemarkData()
                data.add(remarkData)

                for (i in 0 until remark.works.size) {
                    workCount++
                    val work = remark.works[i]
                    when (work.status) {
                        /**
                         * заполненеи списков по вкладкам
                         */
                        WorkStatus.ACCEPTED_MASTER -> masterAcceptedList.add(work)
                        WorkStatus.ACCEPTED_RZD -> rzdAcceptedList.add(work)
                        WorkStatus.ACCEPTED_SLD -> sldAcceptedList.add(work)
                    }

                    val workData = WorkData().apply {
                        this.work = work
                        position = i
                        isOtkCheckEnable = (work.status == WorkStatus.ACCEPTED_MASTER || work.status == WorkStatus.ACCEPTED_SLD) && isRoleSld
                        isRzdCheckEnable = (work.status == WorkStatus.ACCEPTED_SLD || work.status == WorkStatus.ACCEPTED_RZD) && isRoleRzd
                        this.remarkData = remarkData
                    }
                    data.add(workData)
                }

                remarkData.apply {
                    masterUnCheckCount = remark.works.size - masterAcceptedList.size - rzdAcceptedList.size - sldAcceptedList.size
                    sldUnCheckCount = remark.works.size - sldAcceptedList.size - rzdAcceptedList.size
                    rzdUnCheckCount = remark.works.size - rzdAcceptedList.size

                    isDeleteBtnEnable = isRoleRzd

                    if (isRoleSld) {
                        isAcceptBtnEnable = (sldUnCheckCount - masterUnCheckCount > 0)
                    } else {
                        isAcceptBtnEnable = (rzdUnCheckCount - sldUnCheckCount > 0)
                    }

                    this.remark = remark
                }
            }
            Pair(data, workCount)
        }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.setLoadingVisibility(false)
                            view.setData(result.first)
                            view.setRemarkCount(result.second)
                        },
                        {
                            view.setLoadingVisibility(false)
                            logger.error(it)
                        })

    }
}