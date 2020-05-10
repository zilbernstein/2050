package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.master.model.WriteOffTmc
import ru.digipeople.locotech.master.ui.activity.AppNavigator
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter.TMCData
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.interactor.AcceptWorkJob
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.interactor.TmcLoader
import ru.digipeople.locotech.master.ui.activity.writeofftmcamount.WriteOffTmcAmountParams
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCBeforeAcceptPresenter @Inject constructor(private val viewState: TMCBeforeAcceptViewState,
                                                   private val workIds: ArrayList<String>,
                                                   private val loader: TmcLoader,
                                                   private val worker: AcceptWorkJob,
                                                   private val navigator: Navigator,
                                                   private val appNavigator: AppNavigator) : BaseMvpViewStatePresenter<TMCBeforeAcceptView, TMCBeforeAcceptViewState>(viewState) {
    //region Disposables
    private var loadDisposables = Disposables.disposed()
    private var acceptWorkDisposable = Disposables.disposed()
    //end Region
    //region Other
    private var tmcList = listOf<WriteOffTmc>()
    //end Region
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}

    fun onScreenShown() {
        /**
         * Загрузка ТМЦ
         */
        loadDisposables.dispose()
        loadDisposables = loader.loadTmc(workIds)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Проверка результата
                     */
                    if (result.isSuccessful) {
                        tmcList = result.data!!
                        view.setData(tmcList)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.setLoadingVisibility(false)
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.localizedMessage)
                })
    }
    /**
     * Нажатие принять
     */
    fun onCheckBtnClecked() {
        val overrunWork = tmcList.firstOrNull { it.tmcList.firstOrNull() { it.assigned > it.normal } != null }
        view.showWriteOffDialog(overrunWork != null)
    }
    /**
     * Нажатие на элемент
     */
    fun onItemClicked(tmcData: TMCData) {
        navigator.navigateToWriteOffTmcAmount(WriteOffTmcAmountParams(tmcData.id, tmcData.tmc.id, tmcData.tmc.name, tmcData.tmc.assigned, tmcData.tmc.normal, tmcData.tmc.uom))
    }

    // Костыль - метод должен быть не здесь
    fun onAcceptCLicked() {
        acceptWorks()
    }
    /**
     * Принять работы
     */
    private fun acceptWorks() {
        acceptWorkDisposable.dispose()
        acceptWorkDisposable = worker.acceptWork(workIds)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        appNavigator.navigateToPerformance()
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
}