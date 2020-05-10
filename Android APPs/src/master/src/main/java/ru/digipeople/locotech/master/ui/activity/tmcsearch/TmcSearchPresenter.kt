package ru.digipeople.locotech.master.ui.activity.tmcsearch

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.master.model.TMC
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcamount.TmcAmountParams
import ru.digipeople.locotech.master.ui.activity.tmcsearch.interactor.TmcLoader
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcSearchPresenter @Inject constructor(viewState: TmcSearchViewState,
                                             private val navigator: Navigator,
                                             private val workId: String,
                                             private val tmcIdList: ArrayList<String>,
                                             private val tmcLoader: TmcLoader) : BaseMvpViewStatePresenter<TmcSearchView, TmcSearchViewState>(viewState) {
    /**
     * Подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(TmcSearchPresenter::class)

    private val tmcIdSet = tmcIdList.toSet()
    private var queryStr = ""
    private var loadDisposable: Disposable = Disposables.disposed()
    private var inStock = false
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}
    /**
     * Действия при уничтожени
     */
    override fun destroy() {
        loadDisposable.dispose()
    }
    /**
     * Обработка нажатия назад
     */
    fun onBackBtnClicked() {
        navigator.navigateBack()
    }

    fun onInStockCheckedChange(isInStock: Boolean) {
        this.inStock = isInStock
        loadData(queryStr, tmcIdSet, inStock)
    }
    /**
     * Переход к сканеру
     */
    fun onScanBtnClicked(reqCode: Int){
        navigator.navigateToScanActivity(reqCode)
    }
    /**
     * Изменение строки поиска
     */
    fun onQueryStrChanged(queryStr: String) {
        this.queryStr = queryStr
        loadData(queryStr, tmcIdSet, inStock)
    }
    /**
     * нажатие на элемент вписка
     */
    fun itemClicked(tmc: TMC) {
        navigator.navigateToTmcAmount(TmcAmountParams(workId, tmc.id, tmc.name, 0.0, tmc.workshop, tmc.stockRest, tmc.uom))
    }

    fun onScreenShown() {
        loadData(queryStr, tmcIdSet, inStock)
    }
    /**
     * Загрузка данных
     */
    private fun loadData(queryStr: String, tmcIdSet: Set<String>, stockAvailable: Boolean) {
        loadDisposable.dispose()
        if (queryStr.length < 3) {
            view.setLoadingVisible(false)
            view.setItems(emptyList())
            view.setNoDataMsgVisible(false)
            view.setHeaderVisible(false)
            return
        }
        loadDisposable = tmcLoader.loadTmcList(queryStr, tmcIdSet, stockAvailable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisible(false)
                    view.setItems(result.tmcList)
                    view.setNoDataMsgVisible(result.tmcList.isEmpty())
                    view.setHeaderVisible(result.tmcList.isNotEmpty())
                    if (!result.isSuccessful) {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError)
                    }
                }, { logger.error(it) })
    }
}