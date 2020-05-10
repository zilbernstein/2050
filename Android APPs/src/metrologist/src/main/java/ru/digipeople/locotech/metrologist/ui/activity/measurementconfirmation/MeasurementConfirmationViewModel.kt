package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.Remark
import ru.digipeople.locotech.metrologist.data.model.WheelPairShort
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.interactor.MeasurementConfirmationInteractor
import ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.interactor.MeasurementConfirmationLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View model замечаний
 */
class MeasurementConfirmationViewModel @Inject constructor(
        private val measurementConfirmationLoader: MeasurementConfirmationLoader,
        private val completeMeasurementInteractor: MeasurementConfirmationInteractor,
        private val appNavigator: AppNavigator,
        private val flowStorage: MeasurementFlowStorage
) : BaseViewModel() {
    //region LiveData
    val measurementLiveData = MutableLiveData<Measurement>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val wheelPairsLiveData = MutableLiveData<List<WheelPairShort>>()
    val remarksLiveData = MutableLiveData<List<Remark>>()
    val remarksNoDataLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    //endregion
    private var measurementConfirmationDisposable = Disposables.disposed()
    private var completeMeasurementDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        measurementLiveData.value = flowStorage.measurement
        load()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        measurementConfirmationDisposable.dispose()
    }
    /**
     * Загрузка данных
     */
    fun load() {
        measurementConfirmationDisposable = measurementConfirmationLoader.load(flowStorage.measurement.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        wheelPairsLiveData.value = result.wheelPairs
                        remarksLiveData.value = result.remarks
                        remarksNoDataLiveData.value = result.remarks.isEmpty()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * принять
     */
    fun onApplyBtnClicked() {
        completeMeasurementDisposable = completeMeasurementInteractor.post(flowStorage.measurement.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .doOnSuccess { appNavigator.navigateBackToMeasurementTypes() }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}