package ru.digipeople.locotech.master.ui.activity.checkwork

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.checkwork.interactor.CheckWorkJob
import ru.digipeople.locotech.master.ui.activity.checkwork.interactor.CheckWorkLoader
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
class CheckWorkPresenter @Inject constructor(viewState: CheckWorkViewState,
                                             private val navigator: Navigator,
                                             private val callingType: CheckWorkCallingType,
                                             private val checkWorkJob: CheckWorkJob,
                                             private val workLoader: CheckWorkLoader) : BaseMvpViewStatePresenter<CheckWorkView, CheckWorkViewState>(viewState) {
    //region Other
    private var dataList = emptyList<Work>()
    private var checkWorkDisposable = Disposables.disposed()
    private var transformListDisposable = Disposables.disposed()
    //endRegion
    /**
     * Операции при инициализации
     */
    override fun onInitialize() {
        dataList = mutableListOf()
    }
    /**
     * Операции при отрисовке экрана
     */
    fun onScreenShown() {
        when (callingType) {
            CheckWorkCallingType.CALLING_WORK_ACCEPT -> {
                loadWorkForMasterAccept()
            }
            CheckWorkCallingType.CALLING_WORK_APPROVE -> {
                loadWorkForApprove()
            }
            CheckWorkCallingType.CALLING_WORK_START -> {
                loadWorkForStart()
            }
        }
    }
    /**
     * Операции при нажатии на кнопку
     */
    fun buttonClicked() {
        /**
         * определение типа операции
         */
        when (callingType) {
            /**
             * принятие работы
             */
            CheckWorkCallingType.CALLING_WORK_ACCEPT -> {
                val workIds = ArrayList<String>()
                dataList.forEach { workIds.add(it.id) }
                navigator.navigateToTmcBeforeAccept(workIds)
            }
            /**
             * согласование работы
             */
            CheckWorkCallingType.CALLING_WORK_APPROVE -> {
                approveWork()
            }
            /**
             * старт работы
             */
            CheckWorkCallingType.CALLING_WORK_START -> {
                startWork()
            }
        }
    }
    /**
     * удаление работы
     */
    fun deleteButtonClicked(work: Work) {
        transformListDisposable = Single.fromCallable {
            dataList = dataList.filter { it != work }
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setData(dataList)
                }, {})
    }
    /**
     * переход в фотогалерею
     */
    fun onPhotoButtonClicked(work: Work) {
        navigator.navigateToWorkPhotoGallery(work.id)
    }
    /**
     * загрузка работ для согласования
     */
    private fun loadWorkForApprove() {
        checkWorkDisposable = workLoader.loadWorkForApprove()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        dataList = result.equipment!!
                        view.setData(dataList)
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * загрузка работ для принятия мастером
     */
    private fun loadWorkForMasterAccept() {
        checkWorkDisposable = workLoader.loadWorkForMasterAccept()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        dataList = result.equipment!!
                        view.setData(dataList)
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * загрузка работ для старта
     */
    private fun loadWorkForStart() {
        checkWorkDisposable = workLoader.loadWorkForStart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        /**
                         * обработка результата
                         */
                        dataList = result.equipment!!
                        view.setData(dataList)
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * согласование работы
     */
    private fun approveWork() {
        /**
         * проверка, не пустой ли список работ
         */
        if (dataList.isEmpty()) {
            return
        }
        val idList = mutableListOf<String>()
        dataList.forEach { idList.add(it.id) }

        checkWorkDisposable = checkWorkJob.approveWork(idList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            if (result.isSuccessful) {
                                navigator.navigateBack()
                            } else {
                                /**
                                 * отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * принятие работ
     */
    private fun acceptWork() {
        /**
         * проверка, не пустой ли список работ
         */
        if (dataList.isEmpty()) {
            return
        }
        val idList = mutableListOf<String>()
        dataList.forEach { idList.add(it.id) }

        checkWorkDisposable = checkWorkJob.acceptWork(idList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            if (result.isSuccessful) {
                                navigator.navigateBack()
                            } else {
                                /**
                                 * отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.showError(it.message!!)
                })
    }
    /**
     * старт работ
     */
    private fun startWork() {
        /**
         * проверка, не пустой ли список работ
         */
        if (dataList.isEmpty()) {
            return
        }
        val idList = mutableListOf<String>()
        dataList.forEach { idList.add(it.id) }

        checkWorkDisposable = checkWorkJob.startWork(idList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            if (result.isSuccessful) {
                                navigator.navigateBack()
                            } else {
                                /**
                                 * отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.showError(it.message!!)
                })
    }
}