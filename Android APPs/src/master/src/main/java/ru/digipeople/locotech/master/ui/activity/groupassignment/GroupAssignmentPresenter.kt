package ru.digipeople.locotech.master.ui.activity.groupassignment

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.master.model.Brigade
import ru.digipeople.locotech.master.model.WorkForWorkerAssignment
import ru.digipeople.locotech.master.model.WorkGroup
import ru.digipeople.locotech.master.model.WorkerForBrigade
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.BrigView
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.BrigadeAdapterData
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig.WorkerView
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.GroupAdapterData
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.GroupView
import ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group.WorkView
import ru.digipeople.locotech.master.ui.activity.groupassignment.iteractor.WorkAndBrigadesLoader
import ru.digipeople.locotech.master.ui.activity.groupassignment.iteractor.WorkAndBrigadesWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер группового назначения исполнителей
 *
 * @author Sostavkin Grisha
 */
class GroupAssignmentPresenter @Inject constructor(urgentViewState: GroupAssignmentViewState,
                                                   private val workAndBrigadeLoader: WorkAndBrigadesLoader,
                                                   private val workAndBrigadesWorker: WorkAndBrigadesWorker)
    : BaseMvpViewStatePresenter<GroupAssignmentView, GroupAssignmentViewState>(urgentViewState) {

//    private var data = WorkAndBrigadeForAssignmentRepository.INSTANCE

    private var worksList = listOf<WorkGroup>()
    private var brigadesList = listOf<Brigade>()

    private var groupAdapterData: GroupAdapterData? = null
    private var brigadeAdapterData: BrigadeAdapterData? = null
    private var convertToGroupAdapterDataDisposable = Disposables.disposed()
    private var convertToBrigAdapterDataDisposable = Disposables.disposed()
    private var workAndBrigadeDisposable = Disposables.disposed()
    private var sentWorkAndBrigadeDisposable = Disposables.disposed()

    private var workSet: HashSet<WorkForWorkerAssignment> = hashSetOf()
    private var workerSet: HashSet<WorkerForBrigade> = hashSetOf()

    private var currentBrigade: Brigade? = null
    private var currentWorkGroup: WorkGroup? = null

    private var totalTime = 0L
    //TODO хардкод переделать когда будет готов параметр на сервере
    private var workerLimit = 3
    /**
     * действия при инициализации презентера
     */
    override fun onInitialize() {}
    /**
     * действия при отрисовке экрана
     */
    fun onScreenShown() {
        loadData()
        workSet.clear()
        workerSet.clear()
        totalTime = 0L
        currentBrigade = null
        currentWorkGroup = null
    }
    /**
     * обработка нажатия на группу
     */
    fun onGroupClicked(groupView: GroupView) {
        if (currentWorkGroup == null) {
            currentWorkGroup = groupView.group
            setGroupChecked(groupView)
        } else {
            setGroupChecked(groupView)
        }
        /**
         * если время загрузки меньше 0, то показываем 0. Заглушка, исправляющая косяк некорректного расчета нагрузки на сервере
         */
        if (totalTime < 0) totalTime = 0

        recountData()
        checkSelectedGroup(groupView.group)
    }
    /**
     * расчет нагрузки при выборе группы
     */
    private fun setGroupChecked(groupView: GroupView) {
        if (workSet.containsAll(groupView.group.workListEntity)) {
            if (workSet.isEmpty()) {
                currentWorkGroup = null
            }
            groupView.group.workListEntity.forEach {
                workSet.remove(it)
                if (it.isSelected) {
                    totalTime -= ((it.timeLimit * it.workPercent)) / 100
                    it.isSelected = false
                } else {
                    it.isSelected = false
                }
            }
        } else {
            groupView.group.workListEntity.forEach {
                if (!it.isSelected) {
                    totalTime += ((it.timeLimit * it.workPercent)) / 100
                }
                it.isSelected = true
                workSet.add(it)
            }
        }
    }
    /**
     * обработка нажатия на работу
     */
    fun onWorkClicked(workView: WorkView) {
        if (currentWorkGroup == null) {
            currentWorkGroup = workView.workGroup
            setWorkChecked(workView)
        } else {
            setWorkChecked(workView)
        }

        if (totalTime < 0) totalTime = 0

        recountData()
        checkSelectedGroup(workView.workGroup)
    }
    /**
     * расчет нагрузки при выборе работы
     */
    private fun setWorkChecked(workView: WorkView) {
        if (workSet.contains(workView.workForWorker)) {
            workSet.remove(workView.workForWorker)
            if (workSet.isEmpty()) {
                currentWorkGroup = null
            }
            totalTime -= ((workView.workForWorker.timeLimit * workView.workForWorker.workPercent)) / 100
            workView.workForWorker.isSelected = false
        } else {
            workView.workForWorker.isSelected = true
            totalTime += ((workView.workForWorker.timeLimit * workView.workForWorker.workPercent)) / 100
            workSet.add(workView.workForWorker)
        }
    }
    /**
     * обработка нажатия на бригаду
     */
    fun onBrigClicked(brigView: BrigView) {
        when {
            currentBrigade == null -> {
                currentBrigade = brigView.brigade
                setBrigChecked(brigView)
            }
            brigView.brigade.id == currentBrigade!!.id -> setBrigChecked(brigView)
            else -> view.showBrigadeError()
        }
    }
    /**
     * установка данных при выборе группы
     */
    private fun setBrigChecked(brigView: BrigView) {
        if (workerSet.containsAll(brigView.brigade.brigadeWorkerList)) {
            currentBrigade = null
            brigView.brigade.brigadeWorkerList.forEach {
                workerSet.remove(it)
                it.isChecked = false
                it.newLoadPercent = 0f
            }
        } else {
            brigView.brigade.brigadeWorkerList.forEach {
                if (it.isAccessible) {
                    if (workerLimit > workerSet.size) {
                        it.isChecked = true
                        workerSet.add(it)
                    }
                } else {
                    view.showWorkersError()
                }
            }
        }
        brigView.brigade.isSelected = workerSet.containsAll(brigView.brigade.brigadeWorkerList)
        recountData()
    }
    /**
     * обработка нажатия на исполнителя
     */
    fun onWorkerClicked(workerView: WorkerView) {
        when {
            currentBrigade == null -> {
                currentBrigade = workerView.brigade
                setWorkerChecked(workerView)
            }
            workerView.brigade.id == currentBrigade!!.id -> setWorkerChecked(workerView)
            else -> view.showBrigadeError()
        }
    }
    /**
     * расчет при выборе исполнителя
     */
    private fun setWorkerChecked(workerView: WorkerView) {
        if (workerSet.contains(workerView.worker)) {
            workerSet.remove(workerView.worker)
            if (workerSet.isEmpty()) {
                currentBrigade = null
            }
            workerView.worker.isChecked = false
            workerView.worker.newLoadPercent = 0f
        } else {
            if (workerView.worker.isAccessible) {
                if (workerLimit > workerSet.size) {
                    workerView.worker.isChecked = true
                    workerSet.add(workerView.worker)
                } else {
                    view.showMaxWorkersError()
                }
            } else {
                view.showWorkerError()
            }
        }
        workerView.brigade.isSelected = workerSet.containsAll(workerView.brigade.brigadeWorkerList)
        recountData()
    }
    /**
     * обработка назначения
     */
    fun onAssignmentClicked() {
        when {
            workSet.isEmpty() -> view.showWorkEmptyError()
            workerSet.isEmpty() -> view.showWorkerEmptyError()
            else -> sentWorkAndBrigadeDisposable = Single.fromCallable {
                val workIds = mutableListOf<String>()
                workSet.forEach { w -> if (w.isSelected) workIds.add(w.id) }

                val workerIds = mutableListOf<String>()
                workerSet.forEach { w -> if (w.isChecked) workerIds.add(w.performer.id) }

                return@fromCallable Pair(workIds, workerIds)
            }.flatMap { (workIds, workerIds) ->
                workAndBrigadesWorker.assignWorkersGroupForWorks(workIds, workerIds)
            }.subscribeOn(AppSchedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.setLoadingVisibility(true) }
                    .subscribe({ result ->
                        /**
                         * обработка результата
                         */
                        view.setLoadingVisibility(false)
                        if (result.isSuccessful) {
                            loadData()
                            workerSet.clear()
                            workSet.clear()
                            totalTime = 0L
                        } else {
                            /**
                             * отображение ошибки
                             */
                            view.showError(result.userError.message)
                        }
                    }, {
                        view.setLoadingVisibility(false)
                        view.showError(it.message!!)
                    })
        }
    }
    /**
     * выбор группы
     */
    private fun checkSelectedGroup(workGroup: WorkGroup) {
        workGroup.isSelected = workSet.containsAll(workGroup.workListEntity)
        convertDataToGroupAdapter(worksList)
    }
    /**
     * пересчет нагрузки
     */
    private fun recountData() {
        if (currentBrigade == null && currentBrigade == null) {
            convertDataToBrigAdapter(brigadesList)
            return
        }

        workerSet.forEach {
            val correction = totalTime * 100 / (workerSet.size * it.shiftDuration * 1000 * 60 * 60)
            it.newLoadPercent = it.workLoad + correction
        }
        convertDataToBrigAdapter(brigadesList)
    }
    /**
     * получение списка сотрудников и работ
     */
    private fun loadData() {
        /*worksList = data.workGroupList
        brigadesList = data.list
        convertDataToBrigAdapter(brigadesList)
        convertDataToGroupAdapter(worksList)*/
        workAndBrigadeDisposable.dispose()
        workAndBrigadeDisposable = workAndBrigadeLoader.loadWorkAndBrigade()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        brigadesList = result.workAndBrigadeList!!.brigadeList
                        worksList = result.workAndBrigadeList.workGroupList
                        workerLimit = result.workAndBrigadeList.workerLimit
                        convertDataToBrigAdapter(brigadesList)
                        convertDataToGroupAdapter(worksList)
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.setLoadingVisibility(false)
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * конвертирование данных для адаптера групп
     */
    private fun convertDataToGroupAdapter(list: List<WorkGroup>) {
        convertToGroupAdapterDataDisposable = Single.fromCallable {
            groupAdapterData = GroupAdapterData()
            list.forEach { workGroup ->
                val titleView = GroupView(workGroup)
                groupAdapterData!!.add(titleView)
                workGroup.workListEntity.forEach {
                    val groupView = WorkView(workGroup, it)
                    groupAdapterData!!.add(groupView)
                }
            }
            groupAdapterData!!
        }.subscribeOn(AppSchedulers.background())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    /**
                     * загрузка данных
                     */
                    view.setLoadingVisibility(false)
                    view.setDataToGroupAdapter(it)
                }, {
                    view.setLoadingVisibility(false)
                })
    }
    /**
     * конвертирование данных для адаптера бригад
     */
    private fun convertDataToBrigAdapter(list: List<Brigade>) {
        convertToBrigAdapterDataDisposable = Single.fromCallable {
            brigadeAdapterData = BrigadeAdapterData()
            list.forEach { brigade ->
                val titleView = BrigView(brigade)
                brigadeAdapterData!!.add(titleView)
                brigade.brigadeWorkerList.forEach { worker ->
                    val workerView = WorkerView(brigade, worker)
                    brigadeAdapterData!!.add(workerView)
                }
            }
            brigadeAdapterData!!
        }.subscribeOn(AppSchedulers.background())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    /**
                     * загрузка данных
                     */
                    view.setLoadingVisibility(false)
                    view.setDataToBrigAdapter(it)
                }, {
                    view.setLoadingVisibility(false)
                })
    }
}