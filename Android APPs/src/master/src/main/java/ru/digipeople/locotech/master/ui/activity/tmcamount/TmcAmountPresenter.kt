package ru.digipeople.locotech.master.ui.activity.tmcamount

import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcamount.interactor.TMCWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcAmountPresenter @Inject constructor(viewState: TmcAmountViewState,
                                             private val navigator: Navigator,
                                             private val params: TmcAmountParams,
                                             private val tmcWorker: TMCWorker) : BaseMvpViewStatePresenter<TmcAmountView, TmcAmountViewState>(viewState) {

    private var editAmountDisposable = Disposables.disposed()
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}
    /**
     * Действия при уничтожении презентера
     */
    override fun destroy() {
        editAmountDisposable.dispose()
    }
    /**
     * Обработка кнопки принять
     */
    fun onCheckBtnClicked(amount: String) {
        editTMCAmount(amount)
    }
    /**
     * Редактирование ТМЦ в работе
     */
    private fun editTMCAmount(amount: String) {
        if (amount.isBlank()) {
            return
        }
        val value = amount.toDoubleOrNull()
        if (value == null || value <= 0){
            return
        }
        editAmountDisposable.dispose()
        editAmountDisposable = tmcWorker.setTmcAmount(params.workId, params.tmcId, value)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        navigator.navigateToTmcList(null)
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