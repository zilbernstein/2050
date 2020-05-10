package ru.digipeople.locotech.inspector.ui.activity.checklist

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.model.ControlServiceOperation
import ru.digipeople.locotech.inspector.model.EquipmentCso
import ru.digipeople.locotech.inspector.model.UserRole
import ru.digipeople.locotech.inspector.model.WorkStatus
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.EquipmentCSOData
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.OperationData
import ru.digipeople.locotech.inspector.ui.activity.checklist.interactor.CheckListLoader
import ru.digipeople.locotech.inspector.ui.activity.checklist.interactor.CheckListWorker
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentParams
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер чек-листа
 *
 * @author Kashonkov Nikita
 */
class CheckListPresenter @Inject constructor(
        viewState: CheckListViewState,
        private val sessionManager: SessionManager,
        private val navigator: Navigator,
        private val loader: CheckListLoader,
        private val worker: CheckListWorker
) : BaseMvpViewStatePresenter<CheckListView, CheckListViewState>(viewState) {
    private var data: List<EquipmentCso> = emptyList()
    //region Disposables
    private var filterDisposable = Disposables.disposed()
    private var loadDisposable = Disposables.disposed()
    private var convertDisposables = Disposables.disposed()
    private var workDisposables = Disposables.disposed()
    // end Region
    // region Other
    private var logger = LoggerFactory.getLogger(CheckListPresenter::class)
    private var filter = CheckListFilterState.FILTER_ALL
    //end region
    /**
     * действия при инициализации презентера
     */
    override fun onInitialize() {
        val signInInfo = sessionManager.requireSignInInfo
        val isRoleRzd = signInInfo.role == UserRole.RZD
        view.setFinishBtnVisibility(isRoleRzd)
        view.setTitle(sessionManager.selectedEquipment.name)
    }

    fun onScreenShown() = getEquipments()
    /**
     * обработка нажатия на фильтр
     */
    fun onFilterClicked(status: CheckListFilterState) {
        filter = status
        filterDisposable.dispose()
        filterDisposable = filterData(filter, data)
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    /**
                     * обработка данных
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
     * обработка нажатия на фото в работе
     */
    fun onPhotoWorkClicked(operation: ControlServiceOperation) {
        navigator.navigateToWorkPhotoGallery(operation.id)
    }
    /**
     * обработк анажатия на сомментарий
     */
    fun onCsoCommentClicked(operation: ControlServiceOperation) {
        navigator.navigateToWorkComment(CommentParams(operation.id, operation.title, operation.comment.text))
    }
    /**
     * обработка нажатия на согласование
     */
    fun onApproveButtonClicked() {
        view.showApproveDialog()
    }
    /**
     * обрабюотка кнопки согласовать в диалоге
     */
    fun onApproveDialogButtonClicked() {
        view.dismissApproveDialog()
    }
    /**
     * обработка выбора работы
     */
    fun onCheckOtkWorkClicked(operationData: OperationData, operationPosition: Int) {
        val signInInfo = sessionManager.requireSignInInfo
        val isRoleSld = signInInfo.role == UserRole.SLD
        if (!isRoleSld) return
        if (operationData.operation.status == WorkStatus.ACCEPTED_SLD) {
            declineSld(operationData, operationPosition)
        } else {
            acceptSld(operationData, operationPosition)
        }
    }
    /**
     * обработка нажатия принять ржд
     */
    fun onCheckRzdWorkClicked(operationData: OperationData, workPosition: Int) {
        val signInInfo = sessionManager.requireSignInInfo
        val isRoleRzd = signInInfo.role == UserRole.RZD
        if (!isRoleRzd) return
        if (operationData.operation.status == WorkStatus.ACCEPTED_RZD) {
            declineRzd(operationData, workPosition)
        } else {
            acceptRzd(operationData, workPosition)
        }
    }
    /**
     * получить список оборудования
     */
    private fun getEquipments() {
        loadDisposable.dispose()
        loadDisposable = loader.getCyclicWorks()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        data = result.data!!
                    } else {
                        /**
                         * отображени ошибки
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
     * филдьтрация данных
     */
    private fun filterData(filterStatus: CheckListFilterState, data: List<EquipmentCso>): Single<List<EquipmentCso>> {
        return when (filterStatus) {
            CheckListFilterState.FILTER_ACTIVE -> {
                Single.fromCallable {
                    val activeData = mutableListOf<EquipmentCso>()
                    data.forEach { equipment ->
                        /**
                         * все оборудование
                         */
                        val filteredOperationList = equipment.csoList.filter { work ->
                            val signInInfo = sessionManager.requireSignInInfo
                            val isRoleSld = signInInfo.role == UserRole.SLD
                            if (isRoleSld) {
                                /**
                                 * принято мастером
                                 */
                                work.status == WorkStatus.ACCEPTED_MASTER || work.status == WorkStatus.ACCEPTED_SLD
                            } else {
                                /**
                                 * принято отк
                                 */
                                work.status == WorkStatus.ACCEPTED_SLD || work.status == WorkStatus.ACCEPTED_RZD
                            }
                        }
                        if (filteredOperationList.isNotEmpty()) {
                            activeData.add(EquipmentCso().apply {
                                title = equipment.title
                                csoList = filteredOperationList
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
     * преобразование и установка данных
     */
    private fun convertAndSetData(list: List<EquipmentCso>) {
        convertDisposables.dispose()
        convertDisposables = Single.fromCallable {
            val data = AdapterData()

            list.forEach { equipment ->
                val masterAcceptedList = mutableListOf<ControlServiceOperation>()
                val rzdAcceptedList = mutableListOf<ControlServiceOperation>()
                val sldAcceptedList = mutableListOf<ControlServiceOperation>()

                val equipmentData = EquipmentCSOData()
                data.add(equipmentData)

                for (i in 0 until equipment.csoList.size) {
                    val operation = equipment.csoList[i]
                    when (operation.status) {
                        /**
                         * сортировка по статусу
                         */
                        WorkStatus.ACCEPTED_MASTER -> masterAcceptedList.add(operation)
                        WorkStatus.ACCEPTED_RZD -> rzdAcceptedList.add(operation)
                        WorkStatus.ACCEPTED_SLD -> sldAcceptedList.add(operation)
                    }

                    val workData = OperationData().apply {
                        this.operation = operation
                        position = i
                        /**
                         * опередление роли
                         */
                        val signInInfo = sessionManager.requireSignInInfo
                        val isRoleSld = signInInfo.role == UserRole.SLD
                        val isRoleRzd = signInInfo.role == UserRole.RZD
                        /**
                         * определение доступа к принятию работы сотрудником ОТК и РЖД
                         */
                        isOtkCheckEnable = (operation.status == WorkStatus.ACCEPTED_MASTER || operation.status == WorkStatus.ACCEPTED_SLD) && isRoleSld
                        isRzdCheckEnable = (operation.status == WorkStatus.ACCEPTED_SLD || operation.status == WorkStatus.ACCEPTED_RZD) && isRoleRzd
                        this.equipment = equipmentData
                    }
                    data.add(workData)
                }

                equipmentData.apply {
                    masterUnCheckCount = equipment.csoList.size - masterAcceptedList.size - rzdAcceptedList.size - sldAcceptedList.size
                    sldUnCheckCount = equipment.csoList.size - sldAcceptedList.size - rzdAcceptedList.size
                    rzdUnCheckCount = equipment.csoList.size - rzdAcceptedList.size

                    this.equipment = equipment
                }
            }
            data
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
                            view.setData(result)
                        },
                        {
                            /**
                             * отображени ошибки
                             */
                            view.setLoadingVisibility(false)
                            logger.error(it)
                        })

    }
    /**
     * принятие отк
     */
    private fun acceptSld(operationData: OperationData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.acceptSld(operationData.operation.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        operationData.operation.status = WorkStatus.ACCEPTED_SLD
                        operationData.equipment.sldUnCheckCount--

                        view.updateWork(operationData, workPosition)
                    } else {
                        /**
                         * отображеие ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * отклонить отк
     */
    private fun declineSld(operaionData: OperationData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.declineSld(operaionData.operation.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        operaionData.operation.status = WorkStatus.ACCEPTED_MASTER
                        operaionData.equipment.sldUnCheckCount++

                        view.updateWork(operaionData, workPosition)
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
     * принять ржд
     */
    private fun acceptRzd(operationData: OperationData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.acceptRzd(operationData.operation.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        operationData.operation.status = WorkStatus.ACCEPTED_RZD
                        operationData.equipment.rzdUnCheckCount--

                        view.updateWork(operationData, workPosition)
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
     * отклонить ржд
     */
    private fun declineRzd(operationData: OperationData, workPosition: Int) {
        workDisposables.dispose()
        workDisposables = worker.declineRzd(operationData.operation.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработа результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        operationData.operation.status = WorkStatus.ACCEPTED_SLD
                        operationData.equipment.rzdUnCheckCount++

                        view.updateWork(operationData, workPosition)
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

}