package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.model.CyclicGroup
import ru.digipeople.locotech.inspector.model.UserRole
import ru.digipeople.locotech.inspector.model.Work
import ru.digipeople.locotech.inspector.model.WorkStatus
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentParams
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionFilterState
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.CyclicGroupData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.WorkData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.interactor.CyclicWorkLoader
import ru.digipeople.locotech.inspector.ui.fragment.intercator.WorkWorker
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер цикловых работ
 *
 * @author Kashonkov Nikita
 */
class CyclicWorksPresenter @Inject constructor(
        viewState: CyclicWorksViewState,
        private val sessionManager: SessionManager,
        private val navigator: Navigator,
        private val workLoader: CyclicWorkLoader,
        private val worker: WorkWorker
) : BaseMvpViewStatePresenter<CyclicWorksView, CyclicWorksViewState>(viewState) {
    private var data: List<CyclicGroup> = emptyList()
    //region Disposable
    private var filterDisposable = Disposables.disposed()
    private var loadDisposable = Disposables.disposed()
    private var convertDisposables = Disposables.disposed()
    private var workDisposables = Disposables.disposed()
    //end Region
    //region Other
    private var logger = LoggerFactory.getLogger(CyclicWorksPresenter::class)
    private var filter = InspectionFilterState.FILTER_ALL
    //end Region
    /**
     * Инициализация
     */
    override fun onInitialize() {}

    override fun destroy() {
        super.destroy()
        loadDisposable.dispose()
        filterDisposable.dispose()
        convertDisposables.dispose()
    }
    /**
     * переход к контрольным точкам
     */
    fun onControlPointClicked(work: Work) {
        navigator.navigateToControlPointActivity(work.id)
    }
    /**
     * переход к замерам
     */
    fun onMeasurementClicked(work: Work) {
        navigator.navigateToMeasurement(work.id, work.title, work.status.code)
    }
    /**
     * переход к фото
     */
    fun onPhotoClicked(work: Work) {
        navigator.navigateToWorkPhotoGallery(work.id)
    }
    /**
     * принять все
     */
    fun onAcceptAllClicked(remark: CyclicGroupData, remarkPosition: Int) {
        when (sessionManager.requireSignInInfo.role) {
            UserRole.SLD -> acceptGroupSld(remark, remarkPosition)
            UserRole.RZD -> acceptGroupRzd(remark, remarkPosition)
        }
    }
    /**
     * принять отк
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
     * принять ржд
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
     * нажатие на комментарий в работе
     */
    fun onWorkCommentClicked(work: Work) {
        navigator.navigateToWorkComment(CommentParams(work.id, work.title, work.comment.text)
        )
    }
    /**
     * изменение строки в фильтре
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
                    convertAndSetData(data, filterStatus)
                },
                        {
                            view.setLoadingVisibility(false)
                            logger.error(it)
                        })
    }


    fun onScreenShown() {
        getWorks()
    }
    /**
     * принять группу замечаний ОТК
     */
    private fun acceptGroupSld(cyclicGroupData: CyclicGroupData, groupPosition: Int) {
        var count = 0
        workDisposables.dispose()
        workDisposables = Single.fromCallable {
            val workIds = mutableListOf<String>()
            cyclicGroupData.cyclicGroup.works.forEach {
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
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        cyclicGroupData.sldUnCheckCount = cyclicGroupData.sldUnCheckCount - count
                        cyclicGroupData.isAcceptBtnEnable = false
                        cyclicGroupData.cyclicGroup.works.forEach {
                            if (it.status == WorkStatus.ACCEPTED_MASTER) {
                                it.status = WorkStatus.ACCEPTED_SLD
                            }
                        }
                    }
                }
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.updateRemark(groupPosition)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Принять ОТК
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
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_SLD
                        workData.group.sldUnCheckCount--
                        workData.group.isAcceptBtnEnable = (workData.group.sldUnCheckCount - workData.group.masterUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Отклонить ОТК
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
                        workData.group.sldUnCheckCount++
                        workData.group.isAcceptBtnEnable = (workData.group.sldUnCheckCount - workData.group.masterUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Принять группу замечаний РЖД
     */
    private fun acceptGroupRzd(cyclicGroupData: CyclicGroupData, groupPosition: Int) {
        var count = 0
        workDisposables.dispose()
        workDisposables = Single.fromCallable {
            val workIds = mutableListOf<String>()
            cyclicGroupData.cyclicGroup.works.forEach {
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
                     * Инициализация
                     */
                    if (result.isSuccessful) {
                        cyclicGroupData.rzdUnCheckCount = cyclicGroupData.rzdUnCheckCount - count
                        cyclicGroupData.isAcceptBtnEnable = false
                        cyclicGroupData.cyclicGroup.works.forEach {
                            if (it.status == WorkStatus.ACCEPTED_SLD) {
                                it.status = WorkStatus.ACCEPTED_RZD
                            }
                        }
                    }
                }
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработк результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.updateRemark(groupPosition)
                    } else {
                        /**
                         * отображение ошибки
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
        val workDataList: MutableList<String> = mutableListOf()
        workDataList.add(workData.work.id)
        workDisposables = worker.acceptAllRzd(workDataList)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_RZD
                        workData.group.rzdUnCheckCount--
                        workData.group.isAcceptBtnEnable = (workData.group.rzdUnCheckCount - workData.group.sldUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Отклонить РЖД
     */
    private fun declineRzd(workData: WorkData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.declineRzd(workData.work.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        workData.work.status = WorkStatus.ACCEPTED_SLD
                        workData.group.rzdUnCheckCount++
                        workData.group.isAcceptBtnEnable = (workData.group.rzdUnCheckCount - workData.group.sldUnCheckCount > 0)
                        view.updateWork(workData, workPosition)
                    } else {
                        /**
                         * Отображние ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Получить список цикловых работ
     */
    private fun getWorks() {
        loadDisposable.dispose()
        loadDisposable = workLoader.getCyclicWorks()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        data = result.data!!.groupList
                    } else {
                        /**
                         * Отображение ошибки
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
                    convertAndSetData(result, filter)
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Фильтрация данных
     */
    private fun filterData(filterStatus: InspectionFilterState, data: List<CyclicGroup>): Single<List<CyclicGroup>> {
        return when (filterStatus) {
            InspectionFilterState.FILTER_ACTIVE -> {
                Single.fromCallable {
                    val activeData = mutableListOf<CyclicGroup>()
                    data.forEach { remark ->
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
                            activeData.add(CyclicGroup().apply {
                                id = remark.id
                                works = activeRemarkWorkList
                                groupName = remark.groupName
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
    private fun convertAndSetData(list: List<CyclicGroup>, filterStatus: InspectionFilterState) {
        convertDisposables.dispose()
        convertDisposables = Single.fromCallable {
            val data = AdapterData()
            var workCount = 0

            val signInInfo = sessionManager.requireSignInInfo
            val isRoleSld = signInInfo.role == UserRole.SLD
            val isRoleRzd = signInInfo.role == UserRole.RZD

            list.forEach { group ->
                val masterAcceptedList = mutableListOf<Work>()
                val rzdAcceptedList = mutableListOf<Work>()
                val sldAcceptedList = mutableListOf<Work>()

                val groupData = CyclicGroupData()
                data.add(groupData)

                for (i in 0 until group.works.size) {
                    workCount++
                    val work = group.works[i]
                    when (work.status) {
                        WorkStatus.ACCEPTED_MASTER -> masterAcceptedList.add(work)
                        WorkStatus.ACCEPTED_RZD -> rzdAcceptedList.add(work)
                        WorkStatus.ACCEPTED_SLD -> sldAcceptedList.add(work)
                    }

                    val workData = WorkData().apply {
                        this.work = work
                        position = i

                        isOtkCheckEnable = (work.status == WorkStatus.ACCEPTED_MASTER || work.status == WorkStatus.ACCEPTED_SLD) && isRoleSld
                        isRzdCheckEnable = (work.status == WorkStatus.ACCEPTED_SLD || work.status == WorkStatus.ACCEPTED_RZD) && isRoleRzd
                        this.group = groupData
                    }
                    data.add(workData)
                }

                groupData.apply {
                    masterUnCheckCount = group.works.size - masterAcceptedList.size - rzdAcceptedList.size - sldAcceptedList.size
                    sldUnCheckCount = group.works.size - sldAcceptedList.size - rzdAcceptedList.size
                    rzdUnCheckCount = group.works.size - rzdAcceptedList.size

                    when (filterStatus) {
                        InspectionFilterState.FILTER_ALL -> {
                            isAcceptBtnEnable = if (isRoleSld) {
                                masterAcceptedList.size != 0 && masterAcceptedList.size - sldAcceptedList.size != 0
                            } else {
                                sldAcceptedList.size != 0 && sldAcceptedList.size - rzdAcceptedList.size != 0
                            }
                        }
                        InspectionFilterState.FILTER_ACTIVE -> {
                            isAcceptBtnEnable = if (isRoleRzd) {
                                masterUnCheckCount == 0 && sldUnCheckCount != 0
                            } else {
                                sldUnCheckCount == 0 && rzdUnCheckCount != 0
                            }
                        }
                    }
                    cyclicGroup = group
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
                             * Обработка результата
                             */
                            view.setLoadingVisibility(false)
                            view.setData(result.first)
                            view.setRemarkCount(result.second)
                        },
                        {
                            /**
                             * Отображение ошибки
                             */
                            view.setLoadingVisibility(false)
                            logger.error(it)
                        })
    }
}