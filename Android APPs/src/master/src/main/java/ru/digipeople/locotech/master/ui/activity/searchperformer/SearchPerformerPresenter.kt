package ru.digipeople.locotech.master.ui.activity.searchperformer

import ru.digipeople.locotech.master.model.PerformerItem
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.searchperformer.interactor.PerformerLoader
import ru.digipeople.locotech.master.ui.activity.searchperformer.interactor.PerformerWorker
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class SearchPerformerPresenter @Inject constructor(viewState: SearchPerformerViewState,
                                                   private val navigator: Navigator,
                                                   private val params: SearchPerformerParams,
                                                   private val performerLoader: PerformerLoader,
                                                   private val performerWorker: PerformerWorker) : BaseMvpViewStatePresenter<SearchPerformerView, SearchPerformerViewState>(viewState) {
    //region const
    private val MAX_WORKERS_COUNT = 3
    //end Region
    //region other
    private lateinit var list: List<PerformerItem>
    private lateinit var notInWorkList: List<PerformerItem>
    private lateinit var inWorkList: List<PerformerItem>
    private var performerDisposable: Disposable = Disposables.disposed()
    private var filterDisposable: Disposable = Disposables.disposed()
    private lateinit var workModel: Work
    //endregion
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {
        workModel = Work().apply {
            id = params.workId
            title = params.workTitle
            normalTime = params.workNormal
            outfitNumber = params.outfitTitle
            workPartPercent = params.outfitPercent
            remainTime = params.workRemain
        }
        view.showWorkModel(workModel)
    }
    /**
     * изменени строки поиска
     */
    fun searchChanged(search: String) {
        filterList(search)
    }
    /**
     * обработка добавления исполнителя
     */
    fun itemAddClicked(performer: PerformerItem) {
        if (inWorkList.size == MAX_WORKERS_COUNT) return

        if (performer.shiftDuration == 0.0) {
            view.showNullShiftError()
            return
        }

        renewPerformerWorkStatus(performer)
        /**
         * Перерасчет нагрузки
         */
        inWorkList.forEach {
            if (it.isInWork) {
                val correction = workModel.normalTime * workModel.workPartPercent / (performer.shiftDuration * 1000 * 60 * 60) * (1.0 / (inWorkList.size) - 1.0 / (inWorkList.size + 1))
                it.newLoadPercent = it.newLoadPercent - correction
            } else {
                it.newLoadPercent = it.loadPercent + workModel.normalTime * workModel.workPartPercent / ((inWorkList.size + 1) * it.shiftDuration * 1000 * 60 * 60)
            }
        }

        performer.newLoadPercent = performer.loadPercent + workModel.normalTime * workModel.workPartPercent / ((inWorkList.size + 1) * performer.shiftDuration * 1000 * 60 * 60)

        fillWorkerLists()
    }
    /**
     * Удаление исполнителя из работы
     */
    fun itemRemoveClicked(performer: PerformerItem) {
        renewPerformerWorkStatus(performer)
        /**
         * Перерасчет нагрузки
         */
        if (inWorkList.size != 1) {
            inWorkList.forEach {
                if (it != performer) {
                    it.newLoadPercent -= workModel.normalTime * workModel.workPartPercent / (performer.shiftDuration * 1000 * 60 * 60) * (1.0 / (inWorkList.size) - 1.0 / (inWorkList.size - 1))
                }
            }
        }

        performer.loadPercent = performer.newLoadPercent - workModel.normalTime * workModel.workPartPercent / (inWorkList.size * performer.shiftDuration * 1000 * 60 * 60)
        fillWorkerLists()
    }
    /**
     * Нажатие добавление исполнителя
     */
    fun addPerformerClicked() {
        setPerformers()
    }

    fun onScreenShown() {
        loadPerformers()
    }
    /**
     * Загрузка списка исполнителей
     */
    private fun loadPerformers() {
        performerDisposable = performerLoader.loadPerformers(params.workId)
                .doOnSuccess { result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        list = result.performers!!
                        list.forEach {
                            it.newLoadPercent = it.loadPercent
                            it.isInNewWork = it.isInWork
                        }
                    } else {

                    }
                }
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            view.setLoadingVisibility(false)
                            if (result.isSuccessful) {
                                list = result.performers!!
                                fillWorkerLists()
                            } else {
                                view.showError(result.userError.message)
                            }
                        },
                        {
                            /**
                             * Отображение ошибки
                             */
                            view.setLoadingVisibility(false)
                            view.showError(it.message!!)
                        })

    }
    /**
     * Назначение исполниетелей
     */
    private fun setPerformers() {
        performerDisposable = Single.fromCallable {
            val idList = mutableListOf<String>()
            inWorkList.forEach {
                idList.add(it.performer.id)
            }
            return@fromCallable idList
        }
                .flatMap {
                    return@flatMap performerWorker.addPerfromers(params.workId, it)
                }
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            view.setLoadingVisibility(false)
                            if (result.isSuccessful) {
                                navigator.navigateBack()
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.message!!)
                })

    }
    /**
     * Изменени статуса
     */
    private fun renewPerformerWorkStatus(performer: PerformerItem) {
        performer.isInNewWork = !performer.isInNewWork
    }
    private fun fillWorkerLists() {
        filterDisposable.dispose()
        filterDisposable = Single.fromCallable {
            notInWorkList = list.filterNot { it.isInNewWork }
            inWorkList = list.filter { it.isInNewWork }
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.setWorkers(notInWorkList)
                            view.setWorkersInWork(inWorkList)
                        }, {})
    }

    /**
     * Фильтрацич списка исполниетелей
     */
    private fun filterList(text: String) {
        filterDisposable.dispose()
        filterDisposable = Single.fromCallable {
            val filteredList = notInWorkList.filter { it.performer.name.contains(text, true) }
            filteredList
        }
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.setLoadingVisibility(false)
                            view.setWorkers(it)
                        }, {})
    }
}