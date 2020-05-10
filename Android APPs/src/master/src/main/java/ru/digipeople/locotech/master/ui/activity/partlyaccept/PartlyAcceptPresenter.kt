package ru.digipeople.locotech.master.ui.activity.partlyaccept

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.partlyaccept.intercator.PartlyAcceptWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер частичной приемки
 *
 * @author Kashonkov Nikita
 */
class PartlyAcceptPresenter @Inject constructor(viewState: PartlyAcceptViewState,
                                                private var navigator: Navigator,
                                                private val params: PartlyAcceptParams,
                                                private val worker: PartlyAcceptWorker): BaseMvpViewStatePresenter<PartlyAcceptView, PartlyAcceptViewState>(viewState){
    //region Other
    private lateinit var workModel: Work
    private var acceptDisposable = Disposables.disposed()
    private var dividedTime = 0L
    //end Region
    /**
     * инициализация
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
     * обработка нажатия на кнопку принять
     */
    fun onAcceptBtnClicked(){
        navigator.navigateToTmcBeforeAcceptForResult(arrayListOf(workModel.id), PartlyAcceptActivity.WRITE_OFF_TMC)
    }
    /**
     * списание ТМЦ
     */
    fun onTmcWroteOff(){
        acceptWork(dividedTime)
    }
    /**
     * действия при изменении процентов
     */
    fun onPercentChanged(percent: String) {
        if(percent.isEmpty()) {
            view.setAcceptButtonEnability(false)
            view.setNewOutfitInfoVisibility(false)
            return
        }

        val per = percent.toInt()
        if(per >= params.outfitPercent || per == 0 ){
            val str = percent.substring(0, percent.length-1)
            view.setPercent(str)
            return
        }

        val remainPercent = params.outfitPercent - per
        view.setNewOutfitPercent(remainPercent)

        dividedTime = params.workNormal*remainPercent/100
        view.setNewOutfitTime(dividedTime)
        /**
         * изменение видимости
         */
        view.setAcceptButtonEnability(true)
        view.setNewOutfitInfoVisibility(true)
    }
    /**
     * принятие работы
     */
    private fun acceptWork(dividedTime: Long) {
        acceptDisposable.dispose()
        acceptDisposable = worker.partlyAcceptWork(params.workId, dividedTime)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if(result.isSuccessful){
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