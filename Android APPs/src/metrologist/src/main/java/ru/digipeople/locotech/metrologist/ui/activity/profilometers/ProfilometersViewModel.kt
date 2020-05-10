package ru.digipeople.locotech.metrologist.ui.activity.profilometers

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.profilometers.interactor.MeasurementsIntoProfilometerLoader
import ru.digipeople.locotech.metrologist.ui.activity.profilometers.interactor.ProfilometersLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View Model профилометров
 */
class ProfilometersViewModel @Inject constructor(private val profilometersLoader: ProfilometersLoader,
                                                 private val measurementsIntoProfilometerLoader: MeasurementsIntoProfilometerLoader,
                                                 private val navigator: AppNavigator,
                                                 private val flowStorage: MeasurementFlowStorage) : BaseViewModel() {
    //region Params
    var donorId = ""
    //endregion
    //region LiveData
    val profilometersLiveData = MutableLiveData<Map<String, List<Measurement>>>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val noDataLiveData = MutableLiveData<Boolean>()
    //endregion
    private var profilometersDisposable = Disposables.disposed()
    private var loadMeasurementsIntoProfilometers = Disposables.disposed()
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
        profilometersDisposable.dispose()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        profilometersDisposable = profilometersLoader.loadProfilometerMeasurements()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    profilometersLiveData.value = result.measurements
                    noDataLiveData.value = result.measurements.isEmpty()
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * Загрузка измерения в профилометр
     */
    private fun loadMeasurementIntoProfilometer(acceptorId: String, donorId: String) {
        loadMeasurementsIntoProfilometers = measurementsIntoProfilometerLoader.load(acceptorId, donorId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        flowStorage.measurementInfo = result.measurementInfo
                        navigator.navigateBack()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    fun onMeasurementClicked(measurement: Measurement) {
        loadMeasurementIntoProfilometer(measurement.id, donorId)
    }
}