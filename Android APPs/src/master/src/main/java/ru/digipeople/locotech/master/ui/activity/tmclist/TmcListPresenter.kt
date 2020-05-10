package ru.digipeople.locotech.master.ui.activity.tmclist

import ru.digipeople.locotech.master.model.TMCInWork
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcamount.TmcAmountParams
import ru.digipeople.locotech.master.ui.activity.tmclist.interactor.TMCLoader
import ru.digipeople.locotech.master.ui.activity.tmclist.interactor.TMCWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcListPresenter @Inject constructor(viewState: TmcListViewState,
                                           private val idWork: String,
                                           private val navigator: Navigator,
                                           private val tmcLoader: TMCLoader,
                                           private val tmcWorker: TMCWorker) : BaseMvpViewStatePresenter<TmcListView, TmcListViewState>(viewState) {

    private var currentList: List<TMCInWork> = listOf()
    private var loadDisposable = Disposables.disposed()
    private var deleteDisposable = Disposables.disposed()
    private lateinit var deletingTmc: TMCInWork
    /**
     * Дейсвия при инициализации
     */
    override fun onInitialize() {}

    fun onScreenShown() {
        getData()
    }
    /**
     * Обработка удаления
     */
    fun onTmcDeleteClicked(tmc: TMCInWork) {
        deletingTmc = tmc
        view.showDeleteDialog()
    }
    /**
     * Обработка подтверждения удаления
     */
    fun onApproveDeleteClicked() {
        deleteTmc(deletingTmc)
    }
    /**
     * Обработка нажатия на количество ТМЦ
     */
    fun onAmountCicked(tmc: TMCInWork) {
        navigator.navigateToTmcAmount(TmcAmountParams(idWork, tmc.id, tmc.name, tmc.requested, tmc.workshop, tmc.stockRest, tmc.uom))
    }
    /**
     * Обработка добавления
     */
    fun onAddBtnClicked() {
        val tmcIdList = ArrayList<String>(currentList.size)
        currentList.mapTo(tmcIdList) { it.id }
        navigator.navigateToTmcSearch(idWork, tmcIdList)
    }
    /**
     * Обработка нажатия назад
     */
    fun onBackBtnClicked() {
        navigator.navigateBack()
    }
    /**
     * Обработка цничтожения
     */
    override fun destroy() {
        loadDisposable.dispose()
        deleteDisposable.dispose()
    }
    /**
     * Получение данных
     */
    private fun getData() {
        loadDisposable.dispose()
        loadDisposable = tmcLoader.loadTmc(idWork)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        currentList = result.tmc!!
                        view.setData(currentList)
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
     * Удаление ТМЦ
     */
    private fun deleteTmc(tmc: TMCInWork) {
        deleteDisposable.dispose()
        deleteDisposable = tmcWorker.deleteTMC(idWork, tmc.id)
                .doOnSuccess { result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        currentList = currentList.filter { it.id != tmc.id }
                    }
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.setData(currentList)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.showError(it.message!!)
                })
        view.setData(currentList)
    }

}