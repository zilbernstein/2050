package ru.digipeople.locotech.master.ui.activity.performance

import android.util.ArrayMap
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.locotech.master.interactor.SetWorkaroundInteractor
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.comment.CommentParams
import ru.digipeople.locotech.master.ui.activity.divide.DivideParams
import ru.digipeople.locotech.master.ui.activity.partlyaccept.PartlyAcceptParams
import ru.digipeople.locotech.master.ui.activity.performance.interactor.WorkJob
import ru.digipeople.locotech.master.ui.activity.performance.interactor.WorksLoader
import ru.digipeople.locotech.master.ui.activity.performance.model.Tab
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerParams
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListParameters
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер исполнения
 *
 * @author Kashonkov Nikita
 */
class PerformancePresenter @Inject constructor(
        viewState: PerformanceViewState,
        private val sessionManager: SessionManager,
        private val subscriptionProvider: SubscriptionProvider,
        private val worksLoader: WorksLoader,
        private val navigator: Navigator,
        private val workJob: WorkJob,
        private val setWorkaroundInteractor: SetWorkaroundInteractor) : BaseMvpViewStatePresenter<PerformanceView, PerformanceViewState>(viewState) {

    private val logger = LoggerFactory.getLogger(PerformancePresenter::class)

    private var tabLists = mutableMapOf<Tab, List<Work>>()
    private var dataList: List<Work> = mutableListOf()
    private var selectedTab = Tab.IN_WORK
    private var loadDisposable = Disposables.disposed()
    private val compositeDisposable = CompositeDisposable()
    private var subscribeToWorkDisposable = Disposables.disposed()
    private var workStatusDisposable = Disposables.disposed()
    private var returnedWork: Work? = null

    //region Filters
    private var showWithTmcOnly = false
    private var showWithMeasurementsOnly = false
    private var showWithMpiOnly = false
    // endregion
    /**
     * инициализация
     */
    override fun onInitialize() {
        filterWorks()
        view.setActiveTab(selectedTab)
    }
    /**
     * уничтожение презентера
     */
    override fun destroy() {
        compositeDisposable.dispose()
        loadDisposable.dispose()
        subscribeToWorkDisposable.dispose()
        workStatusDisposable.dispose()
        super.destroy()
    }
    /**
     * действия при отображении экрана
     */
    fun onScreenShown() {
        loadWorks()
        subscribeToWork()

        view.setEquipmentName(sessionManager.selectedEquipment?.name ?: "")
    }
    /**
     * выбор вкладки
     */
    fun onTabClicked(tab: Tab) {
        this.selectedTab = tab
        view.setActiveTab(selectedTab)
        updateDisplayedList()
        /**
         * установка кнопок в зависимости от вкладки
         */
        when (tab) {
            Tab.FINISHED -> view.setActionButtonForDoneWork()
            Tab.NEW -> view.setActionButtonForNewWork()
            else -> view.hideActionButton()
        }
    }
    /**
     * обработка изменнения комментария
     */
    fun onEditCommentClicked(work: Work) {
        editComment(work)
    }
    /**
     * обработка нажатия на ТМЦ
     */
    fun onTmcClicked(work: Work) {
        navigator.navigateToTmcList(TmcListParameters(work.id, work.title))
    }
    /**
     * обработка нажатия на фото
     */
    fun onPhotoClicked(work: Work) {
        navigator.navigateToWorkPhotoGallery(work.id)
    }
    /**
     * обработка нажатия на разделение работы
     */
    fun onDivideClicked(work: Work) {
        navigator.navigateToDivide(DivideParams(work.id, work.title, work.workPartPercent, work.normalTime, work.outfitNumber))
    }
    /**
     * обработка нажатия на частичную приемку
     */
    fun onPartlyAcceptClicked(work: Work) {
        navigator.navigateToPartlyAccept(PartlyAcceptParams(work.id, work.title, work.workPartPercent, work.normalTime, work.outfitNumber))
    }
    /**
     * обработка нажатия на замеры
     */
    fun onMeasurementClicked(work: Work) {
        navigator.navigateToMeasurement(work.id, work.title, work.status.code)
    }
    /**
     * остановка работы
     */
    fun onStoppedWorkClicked(work: Work) {
        stopWork(work.id)
    }
    /**
     * принятие работы
     */
    fun onAcceptWorkClicked(work: Work) {
        acceptWork(work)
    }
    /**
     * запуск работы
     */
    fun onStartWorkClicked(work: Work) {
        startWork(work)
    }
    /**
     * возврат работы
     */
    fun onReturnWorkClicked(work: Work) {
        returnedWork = work
        showCommentBeforeReturn(work)
    }
    /**
     * обработка принять все
     */
    fun onAcceptAllButtonClicked() {
        acceptAllWorks()
    }
    /**
     * обработка начать все
     */
    fun startAllWorkButtonClicked() {
        startAllWork()
    }
    /**
     * добавлние комментария при возврате работы
     */
    fun onCommentAddedToReturnedWork() {
        startReturnedWork()
    }
    /**
     * добавление комментария при ошибке
     */
    fun onCommentAddedToReturnWorkMistake() {
        returnedWork = null
    }
    /**
     * добавлние исполнителя
     */
    fun onPerformanceClicked(work: Work) {
        addPerformer(work)
    }
    /**
     * обходное решение
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
                            loadWorks()
                        } else {
                            /**
                             * сообщение об ошибке
                             */
                            view.showError(result.userError)
                        }
                    }, { logger.error(it) })
        }
    }
    /**
     * редактирование комментария
     */
    private fun editComment(work: Work) {
        val commentParams = CommentParams(work.id, work.title, work.comment?.text)
        navigator.navigateToWorkComment(commentParams)
    }
    /**
     * показ комментария перед возвратом
     */
    private fun showCommentBeforeReturn(work: Work) {
        val commentParams = CommentParams(work.id, work.title, work.comment?.text)
        navigator.navigateToEditCommentForResult(commentParams)
    }
    /**
     * Загрузка списка работ
     */
    private fun loadWorks() {
        loadDisposable.dispose()
        loadDisposable = worksLoader.loadWorks()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    dataList = result.works
                    updateDisplayedList()
                    if (!result.isSuccessful) {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError)
                    }
                }, { logger.error(it) })
    }
    /**
     * принят работу
     */
    private fun acceptWork(work: Work) {
        navigator.navigateToTmcBeforeAccept(arrayListOf(work.id))
    }
    /**
     * начать работу
     */
    private fun startWork(work: Work) {
        if (work.performers.isEmpty()) {
            view.showNoPerformanceError()
            return
        }

        compositeDisposable.add(workJob.startWork(work.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        loadWorks()
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError)
                    }
                }, { logger.error(it) })
        )
    }
    /**
     * запуск возвращенной задачи
     */
    private fun startReturnedWork() {
        startWork(returnedWork!!)
    }
    /**
     * принять все работы
     */
    private fun acceptAllWorks() {
        val works = tabLists.getValue(selectedTab)
        if (works.isEmpty()) {
            view.showAcceptAllError()
        } else {
            navigator.navigateToCheckWorkBeforeAccept()
        }
    }
    /**
     * остановка работы
     */
    private fun stopWork(id: String) {
        compositeDisposable.add(workJob.stopWork(id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        loadWorks()
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError)
                    }
                }, { logger.error(it) })
        )
    }
    /**
     * добавление инсполнителя
     */
    private fun addPerformer(work: Work) {
        navigator.navigateToAddPerformer(SearchPerformerParams(work.id, work.title, work.workPartPercent, work.normalTime, work.remainTime, work.outfitNumber))
    }
    /**
     * начать все работы
     */
    private fun startAllWork() {
        val works = tabLists.getValue(Tab.NEW)
        if (works.any { it.performers.isNotEmpty() }) {
            navigator.navigateToCheckWorkBeforeStart()
        } else {
            view.showEmptyStartListError()
        }
    }

    private fun subscribeToWork() {
        subscribeToWorkDisposable.dispose()
        subscribeToWorkDisposable = subscriptionProvider.workSubscription()
                .observeOn(AppSchedulers.ui())
                .subscribe { loadWorks() }
    }
    /**
     * обновление работ в соответствиис фильтрами
     * ТМЦ
     */
    fun onToggleFilterWithTmc() {
        showWithTmcOnly = !showWithTmcOnly
        view.setTmcFilterChecked(showWithTmcOnly)
        updateDisplayedList()
    }
    /**
     * Замеры
     */
    fun onToggleFilterWithMeasurements() {
        showWithMeasurementsOnly = !showWithMeasurementsOnly
        view.setMeasurementsFilterChecked(showWithMeasurementsOnly)
        updateDisplayedList()
    }
    /**
     * МПИ
     */
    fun onToggleFilterWithMpi() {
        showWithMpiOnly = !showWithMpiOnly
        view.setMpiFilterChecked(showWithMpiOnly)
        updateDisplayedList()
    }
    /**
     * обновлнеи списка работ после фильтрации
     */
    private fun updateDisplayedList() {
        filterWorks()
        view.setDataToAdapter(tabLists.getValue(selectedTab))

        val perTabCount = ArrayMap<Tab, Int>()
        tabLists.entries.forEach {
            perTabCount[it.key] = it.value.size
        }
        view.setPerTabCount(perTabCount)
    }
    /**
     * фильтрация работ по вкладкам
     */
    private fun filterWorks() {
        val inApproveWorks = mutableListOf<Work>()
        val newWorks = mutableListOf<Work>()
        val inTaskWorks = mutableListOf<Work>()
        val inWorkWorks = mutableListOf<Work>()
        val finishedWorks = mutableListOf<Work>()
        val acceptedWorks = mutableListOf<Work>()
        val allWorks = mutableListOf<Work>()

        tabLists[Tab.IN_APPROVE] = inApproveWorks
        tabLists[Tab.NEW] = newWorks
        tabLists[Tab.IN_TASK] = inTaskWorks
        tabLists[Tab.IN_WORK] = inWorkWorks
        tabLists[Tab.FINISHED] = finishedWorks
        tabLists[Tab.ACCEPTED] = acceptedWorks
        tabLists[Tab.ALL] = allWorks
        /**
         * проверка условий фильтров
         */
        dataList.forEach {
            if (showWithTmcOnly && !it.hasTmc)
                return@forEach
            if (showWithMeasurementsOnly && !it.hasMeasurements)
                return@forEach
            if (showWithMpiOnly && !it.hasMpi)
                return@forEach

            val isWorkAdded = when (it.status) {
                WorkStatus.IN_APPROVE -> inApproveWorks.add(it)
                WorkStatus.APPROVED -> newWorks.add(it)
                WorkStatus.IN_TASK -> inTaskWorks.add(it)
                WorkStatus.IN_WORK, WorkStatus.PAUSED, WorkStatus.STOPPED -> inWorkWorks.add(it)
                WorkStatus.DONE -> finishedWorks.add(it)
                WorkStatus.ACCEPTED_MASTER -> acceptedWorks.add(it)
                else -> false
            }

            if (isWorkAdded)
                allWorks.add(it)
        }
    }
}