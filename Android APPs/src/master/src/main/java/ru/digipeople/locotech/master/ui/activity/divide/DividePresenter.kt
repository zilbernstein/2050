package ru.digipeople.locotech.master.ui.activity.divide

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.divide.interactor.DivideWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер разделения работы
 *
 * @author Kashonkov Nikita
 */
class DividePresenter @Inject constructor(viewState: DivideViewState,
                                          private val navigator: Navigator,
                                          private val params: DivideParams,
                                          private val worker: DivideWorker) : BaseMvpViewStatePresenter<DivideView, DivideViewState>(viewState) {
    //region Other
    private lateinit var workModel: Work
    private var divideDisposable = Disposables.disposed()
    private var dividedTime = 0L
    //end Region
    /**
     * операции при инициализации призентора
     */
    override fun onInitialize() {
        workModel = Work().apply {
            id = params.workId
            title = params.workTitle
            outfitNumber = params.outfitTitle
            workPartPercent = params.outfitPercent
            normalTime = params.workNormal
        }
        view.showModel(workModel)
    }
    /**
     * обработка нажатия на кнопку разделить
     */
    fun onDivideBtnClicked() {
        divideWork(dividedTime)
    }
    /**
     * установка времени
     */
    fun onSetTimeCLicked(time: Long) {
        dividedTime = time
        val percent = (time.toFloat() / workModel.normalTime * 100).toInt()

        view.setPercentAfterTimeChanged(percent.toString())
        view.setDivideButtonEnabled(true)
        view.setTime(dividedTime)
    }
    /**
     * обработка нажати я на время
     */
    fun onEditTimeCLicked() {
        view.showTimePickerDialog(dividedTime, workModel.normalTime)
    }
    /**
     * обработка изменения процентов
     */
    fun onPercentChanged(percent: String) {
        /**
         * процент не установлен
         */
        if (percent.isEmpty()) {
            view.setDivideButtonEnabled(false)
            view.hideDividedTime()
            return
        }

        val per = percent.toInt()
        if (per >= params.outfitPercent || per == 0) {
            val str = percent.substring(0, percent.length - 1)
            view.setPercent(str)
            return
        }
        /**
         * определение времени после раздедения
         */
        dividedTime = params.workNormal * per / 100

        view.setDivideButtonEnabled(true)
        view.setTime(dividedTime)
    }
    /**
     * разделение работы
     */
    private fun divideWork(dividedTime: Long) {
        divideDisposable.dispose()
        divideDisposable = worker.divideWork(params.workId, dividedTime)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        navigator.navigateBack()
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }

                }, { error ->
                    view.showError(error.localizedMessage)
                    view.setLoadingVisibility(false)
                })
    }
}