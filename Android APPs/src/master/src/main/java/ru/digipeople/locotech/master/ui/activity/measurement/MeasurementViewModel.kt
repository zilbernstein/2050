package ru.digipeople.locotech.master.ui.activity.measurement

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.master.model.MeasurementStatus
import ru.digipeople.locotech.master.model.MeasurementsTask
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.measurement.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.measurement.interactor.MeasurementsLoader
import ru.digipeople.locotech.master.ui.activity.measurement.interactor.MeasurementsTaskLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View Model замеров
 *
 * @author Sostavkin Grisha
 */
class MeasurementViewModel @Inject constructor(
        private val navigator: Navigator,
        private val measurementsLoader: MeasurementsLoader,
        private val measurementsTaskLoader: MeasurementsTaskLoader
) : BaseViewModel() {
    //region Params
    lateinit var measurementParams: MeasurementParams
    //endregion
    //region LiveData
    val measurementsLiveData = MutableLiveData<AdapterData>()
    val statusLiveData = MutableLiveData<MeasurementStatus>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val measurementTaskDialogLiveData = SingleEventLiveData<Unit>()
    val measurementTaskLiveData = SingleEventLiveData<MeasurementsTask>()
    val workNameLiveData = MutableLiveData<String>()
    //endregion
    //region Other
    private var measurementDisposable = Disposables.disposed()
    private var measurementTaskDisposable = Disposables.disposed()
    private val status: MeasurementStatus
        get() = statusLiveData.value ?: MeasurementStatus.NO_TASK
    //endregion

    override fun onStart() = loadData()
    /**
     * очистка
     */
    override fun onCleared() {
        measurementDisposable.dispose()
        measurementTaskDisposable.dispose()
        super.onCleared()
    }

    //TODO Remove this code and change edit case implementation, now it's too ambiguous
    /**
     * обработка нажатия на замер
     */
    fun onMeasurementItemClicked(measurement: Measurement) {
        navigator.navigateToMeasurementEdit(MeasureParams(
                measurementParams.workName, measurementParams.workId, measurementParams.workStatus,
                measurement.measurementId, measurement.characteristicId, measurement.characteristicName,
                measurement.measurementNorm, measurement.measurementDate, measurement.worker.name,
                measurement.stage, measurement.measurementValue, measurement.valueType,
                measurement.measurementComment.text, measurement.measurementName, measurement.isHardware
        ))
    }
    /**
     * обработка нажатия на кнопку получения апп замера
     */
    fun onStatusBtnClicked() = when (status) {
        MeasurementStatus.NO_TASK,
        MeasurementStatus.RECEIVED -> measurementTaskDialogLiveData.value = Unit
        MeasurementStatus.WAITING -> updateMeasurementsStatus()
    }
    /**
     * обновление экрана
     */
    fun onRefreshClicked() = loadData()

    fun onAssignmentParamsApplied(stage: Stage, sectionNumber: String) {
        measurementTaskDisposable.dispose()
        measurementTaskDisposable = measurementsTaskLoader
                .getMeasurementsTask(measurementParams.workId, stage, sectionNumber)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        statusLiveData.value = result.measurementsTask.measurementsStatus
                        val task = result.measurementsTask
                        measurementTaskLiveData.value = task.apply { measurementsStage = stage }
                    } else {
                        /**
                         * передача ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, {
                    loadingLiveData.value = false
                    logger.error(it)
                })
    }
    /**
     * получение задания
     */
    fun onTaskReceived(task: MeasurementsTask) {
        //Workaround for changing work state only in data set
        val newData = measurementsLiveData.value!!.clone() as AdapterData
        measurementsLiveData.value = newData
    }
    /**
     * обновление статуса запроса
     */
    private fun updateMeasurementsStatus() {
        val measurementTask = measurementTaskLiveData.value ?: return
        measurementDisposable.dispose()
        measurementDisposable = measurementsLoader
                .loadMeasurementsByTask(measurementParams.workId, measurementTask.taskId, measurementTask.measurementsStage)
                // Хак по запросу Ковалевского, т.к. некому править сервер сейчас
                // checkMeasurementsReady выдает кривые данные
                .flatMap { measurementsLoader.loadMeasurementsWithTaskStatus(measurementParams.workId) }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        statusLiveData.value = result.measurementStatus
                        measurementsLiveData.value = result.items
                    } else {
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * загрузка данных
     */
    private fun loadData() {
        measurementDisposable.dispose()
        measurementDisposable = measurementsLoader
                .loadMeasurementsWithTaskStatus(measurementParams.workId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обрабоока результата
                     */
                    loadingLiveData.value = false
                    statusLiveData.value = result.measurementStatus
                    workNameLiveData.value = measurementParams.workName
                    measurementsLiveData.value = result.items
                    noDataLiveData.value = result.items.isEmpty()
                    if (!result.isSuccessful) {
                        /**
                         * передача ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}