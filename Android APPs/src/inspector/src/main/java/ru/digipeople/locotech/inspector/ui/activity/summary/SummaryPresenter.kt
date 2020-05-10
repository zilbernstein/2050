package ru.digipeople.locotech.inspector.ui.activity.summary

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.summary.interactor.SummaryLoader
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер суммарной информации
 *
 * @author Kashonkov Nikita
 */
class SummaryPresenter @Inject constructor(
        viewState: SummaryViewState,
        private val navigator: Navigator,
        private val summaryLoader: SummaryLoader,
        private val sessionManager: SessionManager
) : BaseMvpViewStatePresenter<SummaryView, SummaryViewState>(viewState) {
    private val logger = LoggerFactory.getLogger(SummaryPresenter::class)
    private var summaryDisposable: Disposable = Disposables.disposed()
    /**
     * инициализация
     */
    override fun onInitialize() {}

    override fun destroy() {
        summaryDisposable.dispose()
        super.destroy()
    }

    fun onScreenShown() {
        view.setTitle(sessionManager.selectedEquipment.name)
        /*view.showCyclicAmount(67, 19, 8)
        view.showOtkAmount(23, 0, 0)
        view.showRzdAmount(2, 1, 0)
        view.showCheckListAmount(24, 21, 10)*/
        loadData()
    }
    /**
     * переод к инспекционному контролю
     */
    fun onCyclicWorkClicked() {
        navigator.navigateToInspectionActivity()
    }
    /**
     * переход к чеклисту
     */
    fun onCheckListClicked() {
        navigator.navigateToCheckList()
    }
    /**
     * переход к замечаниям ОТК
     */
    fun onRemarkOtkClicked() {
        navigator.navigateToInspectionActivityWithOtkRemark()
    }
    /**
     * переход к замечаниям РЖД
     */
    fun onRzdRemarkClicked() {
        navigator.navigateToInspectionActivityWithRzdRemark()
    }
    /**
     * переход к печати
     */
    fun onPrintButtonClicked() {
        navigator.navigateToPrint()
    }
    /**
     * загрузка данных
     */
    private fun loadData() {
        summaryDisposable.dispose()
        summaryDisposable = summaryLoader.loadSummary()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.showLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.showLoading(false)
                            if (result.isSuccessful) {
                                view.setData(result.summaryItems)
                            } else {
                                /**
                                 * обработка ошибки
                                 */
                                view.showError(result.userError)
                            }
                        }, { logger.error(it) }
                )
    }
}