package ru.digipeople.locotech.worker.ui.activity.shift

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.locotech.worker.helper.ClientProvider
import ru.digipeople.locotech.worker.ui.activity.shift.interactor.ShiftWorker
import javax.inject.Inject

/**
 * Презентер смены
 *
 * @author Kashonkov Nikita
 */
class ShiftPresenter @Inject constructor(viewState: ShiftViewState,
                                         private val clientProvider: ClientProvider,
                                         private val shiftWorker: ShiftWorker)
    : BaseMvpViewStatePresenter<ShiftView, ShiftViewState>(viewState) {

    private var disposable: Disposable = Disposables.disposed()

    override fun onInitialize() {}

    fun onScreenShown() {
        getData()
    }
    /**
     * Обработка нажатия на кнопку
     */
    fun onWorkButtonClicked() {
        if (clientProvider.client.isInShift) {
            onStopShift()
        } else {
            onStartShift()
        }
    }
    /**
     * Начало работы
     */
    private fun onStartShift() {
        disposable.dispose()
        disposable = shiftWorker.startShift()
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.setLoading(false)
                            if (result.isSuccessful) {
                                clientProvider.client.isInShift = true
                                view.showWorkStatus(true)
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoading(false)
                    view.showError(it.message!!)
                }
                )
    }
    /**
     * Завершить работу
     */
    private fun onStopShift() {
        disposable.dispose()
        disposable = shiftWorker.stopShift()
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * Обрабьотка результата
                             */
                            view.setLoading(false)
                            if (result.isSuccessful) {
                                clientProvider.client.isInShift = false
                                view.showWorkStatus(false)
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoading(false)
                    view.showError(it.message!!)
                }
                )
    }

    private fun getData() {
        view.setUpView(clientProvider.client, clientProvider.client.isInShift)
    }
}