package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.interactor.MeasurementLoader
import ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.interactor.MeasurementSaver
import ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.model.ScreenState
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import java.util.*
import javax.inject.Inject
/**
 * View model детальной информации замера
 */
class MeasurementDetailViewModel @Inject constructor(
        private val measurementLoader: MeasurementLoader,
        private val measurementSaver: MeasurementSaver,
        private val appNavigator: AppNavigator,
        private val flowStorage: MeasurementFlowStorage
) : BaseViewModel() {
    //region Params
    lateinit var categoryId: String
    //endregion
    //region LiveData
    val screenStateLiveData = MutableLiveData<ScreenState>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val changeProfilometerWarningLiveData = SingleEventLiveData<Unit>()
    val setManualModeWarningLiveData = SingleEventLiveData<Unit>()
    //endregion
    private var measurementDetailDisposable = Disposables.disposed()
    private var saveDisposable = Disposables.disposed()
    private var measurementInfoChangesDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        loadData()

        measurementInfoChangesDisposable = flowStorage.measurementInfoChanges
                .observeOn(AppSchedulers.ui())
                .subscribe { updateScreenState() }
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        measurementInfoChangesDisposable.dispose()
        measurementDetailDisposable.dispose()
        saveDisposable.dispose()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        measurementDetailDisposable = measurementLoader.load(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    noDataLiveData.value = !result.isSuccessful
                    if (result.isSuccessful) {
                        flowStorage.measurementInfo = result.measurementInfo
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    fun onChangeProfilometerApproved() {
        appNavigator.navigateToProfilometerMeasurements(flowStorage.measurement.id)
    }
    /**
     * Подтверждение установки ручного режима
     */
    fun onSetManualModeApproved() {
        flowStorage.measurement.isManual = true
        updateScreenState()
    }
    /**
     * Сохранение
     */
    fun onSaveBtnClicked() {
        if (flowStorage.measurementInfo == null) {
            return
        }
        saveDisposable = measurementSaver.save(flowStorage.measurement)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    noDataLiveData.value = !result.isSuccessful
                    if (result.isSuccessful) {
                        flowStorage.measurementInfo = result.measurementInfo
                        updateScreenState()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * Нажатие на профилометр
     */
    fun onProfilometerClicked() {
        changeProfilometerWarningLiveData.value = Unit
    }
    /**
     * нажатие на ручной замер
     */
    fun onManualMeasurementClicked() {
        setManualModeWarningLiveData.value = Unit
    }
    /**
     * нажатие на аппаратный замер
     */
    fun onToolMeasurementClicked() {
        flowStorage.measurement.isManual = false
        updateScreenState()
    }
    /**
     * нажатие на деталку
     */
    fun onDetailBtnClicked() {
        appNavigator.navigateToWheelPairs()
    }
    /**
     * выбор даты и времени
     */
    fun onDateTimeSelected(dateTime: Date) {
        flowStorage.measurement.date = dateTime
        updateScreenState()
    }

    private fun updateScreenState() {
        screenStateLiveData.value = ScreenState(flowStorage.measurement, flowStorage.parameters)
    }
}