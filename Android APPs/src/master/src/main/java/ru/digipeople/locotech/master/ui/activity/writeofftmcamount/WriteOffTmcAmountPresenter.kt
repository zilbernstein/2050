package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.writeofftmcamount.interactor.TMCWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
class WriteOffTmcAmountPresenter @Inject constructor(val viewState: WriteOffTmcAmountViewState,
                                                     private val params: WriteOffTmcAmountParams,
                                                     private val tmcWorker: TMCWorker,
                                                     private val navigator: Navigator) :
        BaseMvpViewStatePresenter<WriteOffTTmcAmountView, WriteOffTmcAmountViewState>(viewState) {

    private var editAmountDisposable = Disposables.disposed()
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}

    override fun destroy() {
        editAmountDisposable.dispose()
    }
    /**
     * Обрабокта нажатия кнопки принять
     */
    fun onCheckBtnClicked(amount: String) {
        editTMCAmount(amount)
    }
    /**
     * изменение количества ТМЦ
     */
    private fun editTMCAmount(amount: String) {
        editAmountDisposable.dispose()
        editAmountDisposable = tmcWorker.setTmcAmount(params.workId, params.tmcId, amount.toDouble())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
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
}