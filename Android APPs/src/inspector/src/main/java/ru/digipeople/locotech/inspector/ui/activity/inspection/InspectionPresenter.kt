package ru.digipeople.locotech.inspector.ui.activity.inspection

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.inspection.interactor.InspectionWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер инспекционного контроля
 * @author Kashonkov Nikita
 */
class InspectionPresenter @Inject constructor(
        viewState: InspectionViewState,
        private val sessionManager: SessionManager,
        private val worker: InspectionWorker
) : BaseMvpViewStatePresenter<InspectionView, InspectionViewState>(viewState) {
    //region Disposable
    private var workDisposable = Disposables.disposed()
    //endregion
    /**
     * Инициализация
     */
    override fun onInitialize() {
        // nop
    }

    fun onScreenShown() {
        view.setTitle(sessionManager.selectedEquipment.name)
    }
    /**
     * Нажатие на РЖД
     */
    fun onRzdBtnClicked() = notifyRzd()
    /**
     * Вызов представителя РДЖД
     **/
    private fun notifyRzd() {
        workDisposable.dispose()
        workDisposable = worker.notifyRzd()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setProgressVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setProgressVisibility(false)
                    if (result.isSuccessful) {
                        view.showSuccessfullyNotifiedMessage()
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, { error ->
                    view.setProgressVisibility(false)
                    view.showError(error.localizedMessage)
                })
    }
}