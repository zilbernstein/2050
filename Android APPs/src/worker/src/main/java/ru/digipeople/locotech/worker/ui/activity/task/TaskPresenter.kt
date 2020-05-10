package ru.digipeople.locotech.worker.ui.activity.task

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.worker.helper.ClientProvider
import ru.digipeople.locotech.worker.interactor.CheckListLoader
import ru.digipeople.locotech.worker.model.MeasurementStatus
import ru.digipeople.locotech.worker.model.MeasurementsTask
import ru.digipeople.locotech.worker.model.WorkStatus
import ru.digipeople.locotech.worker.ui.activity.AppNavigator
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.task.TaskActivity.Companion.DONE_WORK_PHOTO_REQUEST
import ru.digipeople.locotech.worker.ui.activity.task.interactor.*
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер структуры задания
 *
 * @author Kashonkov Nikita
 */
class TaskPresenter @Inject constructor(
        viewState: TaskViewState,
        private val subscriptionProvider: SubscriptionProvider,
        private var taskId: String,
        private val clientProvider: ClientProvider,
        private val workLoader: WorkLoader,
        private val workJob: WorkJob,
        private val measurementsTaskLoader: MeasurementsTaskLoader,
        private val measurementsLoader: MeasurementsLoader,
        private val checklistChecker: ChecklistChecker,
        private val checkListLoader: CheckListLoader,
        private val navigator: Navigator,
        private val appNavigator: AppNavigator) : BaseMvpViewStatePresenter<TaskView, TaskViewState>(viewState) {
    //region Other
    private var loadDisposable = Disposables.disposed()
    private var interactionDisposable = Disposables.disposed()
    private var taskSubscription = Disposables.disposed()
    private var measurementTaskDisposable = Disposables.disposed()
    private var measurementStatusDisposable = Disposables.disposed()
    private var checklistDisposable = Disposables.disposed()
    private var checkChecklistDisposable = Disposables.disposed()
    private lateinit var commentParams: CommentParams

    private var task: MeasurementsTask? = null
    //endRegion

    override fun onInitialize() {}

    fun onScreenShown() {
        getData(taskId)
        subscribeToTask()
    }
    /**
     * обработка нажатия кнопки начать работу
     */
    fun onStartButtonClicked() {
        startWork(taskId)
    }
    /**
     * обработка нажатия кнопки остановить работу
     */
    fun onPauseButtonClicked() {
        if (!clientProvider.client.isInShift) {
            view.showStartShiftMistake()
        } else
            navigator.navigateToPauseWork(taskId, TaskActivity.START_PAUSE_REASON_REQUEST, commentParams)
    }
    /**
     * обработка нажатия кнопки завершить работу
     */
    fun onCheckButtonClicked() {
        doneWork(taskId)
    }
    /**
     * обработка результатат причины приостановки
     */
    fun pauseReasonResultReturned(reasonId: String) {
        pauseWork(taskId, reasonId)
    }
    /**
     * загрузка чеклиста
     */
    fun onChecklistBtnClicked(workId: String) {
        checklistDisposable = checkListLoader.loadChecklist(workId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        if (result.checklist.items.isNotEmpty()) {
                            appNavigator.navigateToChecklist(workId)
                        } else {
                            view.showChecklistError()
                        }
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showMistake(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                })
    }
    /**
     * выбрать все
     */
    fun onCheckChecklist(workId: String) {
        checkChecklistDisposable = checklistChecker.checkAllItemsChecked(workId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        if (result.allChecked) {
                            navigator.navigateToPhotoForResult(workId, DONE_WORK_PHOTO_REQUEST)
                        } else {
                            /**
                             * обработка ошибки
                             */
                            view.showChecklistCheckError()
                        }
                    } else {
                        view.showMistake(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                })
    }
    /**
     * отправка запроса на получение апп замеров
     */
    fun onStatusBtnClicked(workId: String, workName: String, workStatus: WorkStatus) {
        if (task == null) return
        measurementStatusDisposable = measurementsLoader
                .loadMeasurementsByTask(taskId, task!!.taskId, task!!.measurementsStage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.setMeasurementsStatus(result.measurementStatus)
                        if (result.measurementStatus == MeasurementStatus.RECEIVED)
                        {
                            navigator.navigateToMeasure(workId, workName, workStatus)
                        }
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showMistake(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                })
    }
    /**
     * получение задания на проведение апп замеров
     */
    fun onAssignmentParamsApplied(stage: Stage, sectionNumber: String) {
        measurementTaskDisposable.dispose()
        measurementTaskDisposable = measurementsTaskLoader
                .getMeasurementsTask(taskId, stage, sectionNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        val task = result.measurementsTask
                        this.task = task
                        view.setMeasurementsTask(task)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showMistake(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                })
    }
    /**
     * получение данных
     */
    private fun getData(workId: String) {
        loadDisposable = workLoader.loadWork(workId)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.setUpTaskView(result.workDetail)
                        commentParams = CommentParams(workId, result.workDetail.comment?.text)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showMistake(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showMistake(it.message!!)
                })
    }
    /**
     * начать работу
     */
    private fun startWork(workId: String) {
        interactionDisposable.dispose()
        interactionDisposable = workJob.startWork(workId)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    if (result.isSuccessful) {
                        getData(taskId)
                    } else {
                        view.setLoadingVisibility(false)
                        view.showMistake(result.userError.message)
                    }
                }, {
                    /**
                     * обработка ошибки
                     */
                    view.setLoadingVisibility(false)
                    view.showMistake(it.message!!)
                })
    }
    /**
     * завершение работы
     */
    private fun doneWork(workId: String) {
        interactionDisposable.dispose()
        interactionDisposable = workJob.doneWork(workId)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.showBlankView(true)
                    view.setLoadingVisibility(true)
                }
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    if (result.isSuccessful) {
                        navigator.navigateToMyTask()
                        view.showBlankView(false)
                    } else {
                        view.showBlankView(false)
                        view.setLoadingVisibility(false)
                        view.showMistake(result.userError.message)
                    }
                }, {
                    /**
                     * обработка ошибки
                     */
                    view.showBlankView(false)
                    view.setLoadingVisibility(false)
                    view.showMistake(it.message!!)
                })
    }
    /**
     * остановка работы
     */
    private fun pauseWork(workId: String, reasonId: String) {
        interactionDisposable.dispose()
        interactionDisposable = workJob.pauseWork(workId, reasonId)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    if (result.isSuccessful) {
                        getData(taskId)
                    } else {
                        view.setLoadingVisibility(false)
                        view.showMistake(result.userError.message)
                    }
                }, {
                    /**
                     * обработка ошибки
                     */
                    view.setLoadingVisibility(false)
                    view.showMistake(it.message!!)
                })
//        currentTaskProvider.task = null
    }

    private fun subscribeToTask() {
        taskSubscription.dispose()
        taskSubscription = subscriptionProvider.workSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getData(taskId) }
    }
}