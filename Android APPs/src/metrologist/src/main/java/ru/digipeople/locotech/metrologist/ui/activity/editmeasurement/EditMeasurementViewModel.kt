package ru.digipeople.locotech.metrologist.ui.activity.editmeasurement

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.locotech.metrologist.data.model.WheelParam
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.editmeasurement.interactor.PairParamsSaver
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * view model редактирования данных по кп
 *
 * @author Michael Solenov
 */
class EditMeasurementViewModel @Inject constructor(
        private val pairParamsSaver: PairParamsSaver,
        private val appNavigator: AppNavigator,
        private val flowStorage: MeasurementFlowStorage
) : BaseViewModel() {
    //region Params
    var wheelPairPosition = 0
    //endregion
    //region LiveData
    val measurementLiveData = MutableLiveData<Measurement>()
    val wheelParamsLiveData = MutableLiveData<List<WheelParam<*>>>()
    val wheelPairLiveData = MutableLiveData<WheelPair>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    //endregion
    private lateinit var wheelPair: WheelPair
    private var pairParamsDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        wheelPair = flowStorage.wheelPairs[wheelPairPosition]
        measurementLiveData.value = flowStorage.measurement
        wheelPairLiveData.value = wheelPair
        wheelParamsLiveData.value = wheelPair.wheelParams.map { it.copy() }
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        pairParamsDisposable.dispose()
    }
    /**
     * Сохранение замеров
     */
    fun onSaveBtnClicked() {
        pairParamsDisposable = pairParamsSaver.save(wheelPair.id, wheelParamsLiveData.value!!)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        flowStorage.wheelPairs = result.wheelPairs
                        appNavigator.navigateBack()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}