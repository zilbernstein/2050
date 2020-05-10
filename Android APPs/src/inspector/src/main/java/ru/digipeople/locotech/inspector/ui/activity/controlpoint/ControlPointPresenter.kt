package ru.digipeople.locotech.inspector.ui.activity.controlpoint

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.controlpoint.interactor.ControlPointsLoader
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер контрольных точек
 * @author Kashonkov Nikita
 */
class ControlPointPresenter @Inject constructor(
        viewState: ControlPointViewState,
        private val sessionManager: SessionManager,
        private val controlPointsLoader: ControlPointsLoader,
        private val workId: String
) : BaseMvpViewStatePresenter<ControlPointView, ControlPointViewState>(viewState) {
    /**
     * Подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(ControlPointPresenter::class)
    private var controlPointDisposable: Disposable = Disposables.disposed()
    /**
     * Инициализация
     */
    override fun onInitialize() {}

    override fun destroy() {
        controlPointDisposable.dispose()
        super.destroy()
    }

    fun onScreenShown() {
        loadData(workId)
        view.setSectionName(sessionManager.selectedEquipment.name)
    }
    /**
     * Загрузка данных
     */
    private fun loadData(workId: String) {
        controlPointDisposable = controlPointsLoader.loadControlPoints(workId)
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
                                view.setTitle(result.workName)
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