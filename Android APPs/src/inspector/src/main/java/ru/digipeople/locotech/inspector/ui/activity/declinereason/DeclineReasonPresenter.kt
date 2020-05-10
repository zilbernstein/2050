package ru.digipeople.locotech.inspector.ui.activity.declinereason

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.locotech.inspector.data.DataRepository
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonModel
import ru.digipeople.locotech.inspector.ui.activity.declinereason.interactor.DeclineReasonLoader
import ru.digipeople.locotech.inspector.ui.activity.declinereason.interactor.DeclineReasonWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер причин удаления замечания
 *
 * @author Kashonkov Nikita
 */
class DeclineReasonPresenter @Inject constructor(viewState: DeclineReasonViewState,
                                                 private val navigator: Navigator,
                                                 private val declineReasonLoader: DeclineReasonLoader,
                                                 private val declineReasonWorker: DeclineReasonWorker,
                                                 private val remarkId: String)
    : BaseMvpViewStatePresenter<DeclineReasonView, DeclineReasonViewState>(viewState) {
    /**
     * Подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(DeclineReasonPresenter::class)
    private var declinedReasonDisposable: Disposable = Disposables.disposed()
    private var revokeRemarkDisposable: Disposable = Disposables.disposed()
    /**
     * Инициализация
     */
    override fun onInitialize() {}

    override fun destroy() {
        declinedReasonDisposable.dispose()
        revokeRemarkDisposable.dispose()
        super.destroy()
    }

    fun onScreenShown() {
        loadData()
    }
    /**
     * Удаление замечания
     */
    fun onReasonClicked(reason: DeclinedReasonModel) {
        val data = DataRepository.INSTANCE.otkRemarkList
        val remark = data.firstOrNull { it.title == remarkId }
        //data.remove(remark)
        revokeRemarkDisposable = declineReasonWorker.revokeRemark(remarkId, reason.id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.showLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        /**
                         * Обработка результата
                         */
                        { result ->
                            view.showLoading(false)
                            if (result.isSuccessful) {
                                navigator.navigateBack()
                            } else {
                                /**
                                 * Отображение ошиибки
                                 */
                                view.showError(result.userError)
                            }
                        }, { logger.error(it) }
                )
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        declinedReasonDisposable = declineReasonLoader.loadReasons()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.showLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            view.showLoading(false)
                            if (result.isSuccessful) {
                                view.setData(result.declinedReasonItems)
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                view.showError(result.userError)
                            }
                        }, { logger.error(it) }
                )
    }
}