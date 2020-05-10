package ru.digipeople.locotech.worker.ui.activity.measurements

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.measurements.adapter.EditableMeasurement
import ru.digipeople.locotech.worker.ui.activity.measurements.interactor.MeasurementSaver
import ru.digipeople.locotech.worker.ui.activity.measurements.interactor.MeasurementsLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewModel замеров
 */
class MeasurementsViewModel @Inject constructor(
        private val measurementsLoader: MeasurementsLoader,
        private val measurementSaver: MeasurementSaver,
        private val navigator: Navigator
) : BaseViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = MutableLiveData<UserError>()
    val stagesLiveData = MutableLiveData<List<Stage>>()
    val measurementsLiveData = MutableLiveData<List<Measurement>>()

    lateinit var params: MeasurementsParams

    private var measurementSaveDisposable = Disposables.disposed()
    private var measurementsDisposable = Disposables.disposed()
    private var screenStateDisposable = Disposables.disposed()

    private var measurements = mapOf<Stage, List<Measurement>>()
    private var selectedStage: Stage? = null
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        loadData(params.workId)
    }
    /**
     * очистка
     */
    override fun onCleared() {
        measurementsDisposable.dispose()
        measurementSaveDisposable.dispose()
        screenStateDisposable.dispose()
    }
    /**
     * нажатие на комментарий
     */
    fun onCommentClicked(measurement: Measurement) {
        val commentParams = CommentParams(params.workId, measurement.measurementComment.text)
        navigator.navigateToCommentFromMeasures(commentParams, MeasurementsActivity.START_COMMENT_REQUEST, params.workStatus)
    }
    /**
     * обновить экран
     */
    fun onRefreshBtnClicked() = loadData(params.workId)
    /**
     * нажатие на кнопку сохранить
     */
    fun onSaveBtnClicked(measurements: List<EditableMeasurement>) {
        if (measurements.isEmpty()) return
        measurementSaveDisposable = measurementSaver
                .save(params.workId, measurements)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обработка резульатата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        loadData(params.workId)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * Выбор вкладки
     */
    fun onStageSelected(stage: Stage) {
        measurementsLiveData.value = measurements[stage] ?: emptyList()
        selectedStage = stage
    }
    /**
     * Загрузка данных
     */
    private fun loadData(workId: String) {
        measurementsDisposable = measurementsLoader.loadMeasurements(workId)
                .subscribeOn(AppSchedulers.io())
                .doOnSubscribe { loadingLiveData.value = true }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    val (userError, measurements) = result
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        this.measurements = measurements

                        val stages = mutableListOf<Stage>()
                        for ((stage, measurementsByStage) in measurements) {
                            if (measurementsByStage.isNotEmpty())
                                stages.add(stage)
                        }

                        if (selectedStage == null)
                            selectedStage = stages.firstOrNull()

                        //If still unable to determine which stage we should expose
                        //then fill with empty list to display no data placeholder
                        //Else store stages and select defined stage to fill list with particular measurements
                        if (selectedStage == null) {
                            measurementsLiveData.value = emptyList()
                        } else {
                            stagesLiveData.value = stages
                            onStageSelected(selectedStage!!)
                        }
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = userError
                    }
                }, { logger.error(it) })
    }
}