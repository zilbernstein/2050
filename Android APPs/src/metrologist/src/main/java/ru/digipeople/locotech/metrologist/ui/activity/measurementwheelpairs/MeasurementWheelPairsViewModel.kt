package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor.ClearTurningReasonInteractor
import ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor.WheelPairsLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View Model ввода данных замера
 */
class MeasurementWheelPairsViewModel @Inject constructor(
        private val wheelPairsLoader: WheelPairsLoader,
        private val clearTurningReasonInteractor: ClearTurningReasonInteractor,
        private val appNavigator: AppNavigator,
        private val flowStorage: MeasurementFlowStorage
) : BaseViewModel() {
    //region LiveData
    val measurementLiveData = MutableLiveData<Measurement>()
    val wheelPairsLiveData = MutableLiveData<List<WheelPair>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    //endregion
    private var turningReasonDisposable = Disposables.disposed()
    private var wheelPairDisposable = Disposables.disposed()
    private var cloneDisposable = Disposables.disposed()
    private var wheelPairsChangesDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        measurementLiveData.value = flowStorage.measurement
        loadData()

        wheelPairsChangesDisposable = flowStorage.wheelPairsChanges
                .observeOn(AppSchedulers.ui())
                .subscribe { updateWheelPairsUi() }
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        wheelPairDisposable.dispose()
        turningReasonDisposable.dispose()
        cloneDisposable.dispose()
        wheelPairsChangesDisposable.dispose()

    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        wheelPairDisposable = wheelPairsLoader.load(flowStorage.measurement.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    noDataLiveData.value = result.wheelPairs.isEmpty()
                    flowStorage.wheelPairs = result.wheelPairs
                    updateWheelPairsUi()
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    fun onToggleWheelPairTurning(wheelPair: WheelPair, position: Int) {
        if (wheelPair.mustRepair) {
            clearTurningReason(wheelPair)
        } else {
            /**
             * переход к выбору причины обточки
             */
            appNavigator.navigateToTuningReasons(position)
        }
    }
    /**
     * Очистка причины
     */
    private fun clearTurningReason(wheelPair: WheelPair) {
        turningReasonDisposable = clearTurningReasonInteractor.clearReason(wheelPair.id, flowStorage.measurement.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        wheelPair.apply {
                            repairReasonId = ""
                            repairReasonName = ""
                            mustRepair = false
                        }
                        updateWheelPairsUi()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * сохранение
     */
    fun onSaveBtnClicked() {
        appNavigator.navigateToMeasurementConfirmation()
    }
    /**
     * редактирование колесной пары
     */
    fun onEditWheelPairBtnClicked(wheelPair: WheelPair, position: Int) {
        appNavigator.navigateToEditMeasurement(position)
    }

    private fun updateWheelPairsUi() {
        wheelPairsLiveData.value = flowStorage.wheelPairs
    }
}
