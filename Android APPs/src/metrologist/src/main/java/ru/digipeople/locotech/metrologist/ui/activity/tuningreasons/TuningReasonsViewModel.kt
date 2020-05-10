package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.Reason
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.interactor.SetReasonInteractor
import ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.interactor.TuningReasonsLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View model выбора причины обточки
 *
 * @author Michael Solenov
 */
class TuningReasonsViewModel @Inject constructor(
        private val loader: TuningReasonsLoader,
        private val interactor: SetReasonInteractor,
        private val navigator: AppNavigator,
        private val flowStorage: MeasurementFlowStorage
) : BaseViewModel() {
    //region Params
    var wheelPairPosition = 0
    //endregion
    val reasonsLiveData = MutableLiveData<List<Reason>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    private var loadDataDisposable = Disposables.disposed()
    private var setReasonDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        loadData()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        loadDataDisposable.dispose()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        loadDataDisposable = loader.load()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    reasonsLiveData.value = result.reasons
                    noDataLiveData.value = result.reasons.isEmpty()
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * Выбор причины обточки
     */
    fun onReasonClicked(reason: Reason) {
        val wheelPair = flowStorage.wheelPairs[wheelPairPosition]
        val measurement = flowStorage.measurement
        setReasonDisposable = interactor.setProcessingReason(reason.id, wheelPair.id, measurement.id)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        wheelPair.mustRepair = true
                        wheelPair.repairReasonId = reason.id
                        wheelPair.repairReasonName = reason.name
                        // Уведомляем об изменении данных
                        flowStorage.wheelPairs = flowStorage.wheelPairs
                        navigator.navigateBack()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}