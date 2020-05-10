package ru.digipeople.locotech.master.ui.activity.measurement.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.data.api.mapper.MeasurementMapper
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.MeasurementStatus
import ru.digipeople.locotech.master.model.mapper.measurementStatusMapper
import ru.digipeople.locotech.master.ui.activity.measurement.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.measurement.adapter.MeasurementModel
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import java.util.Collections.emptyList
import javax.inject.Inject

/**
 * @author Sostavkin Grisha
 */
class MeasurementsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание маппера
     */
    private val measurementMapper = MeasurementMapper.INSTANCE
    /**
     * загрузка замеров
     */
    fun loadMeasurements(workId: String): Single<Result> {
        return thingsWorxWorker.getWorkMeasurements(workId)
                .map { response ->
                    /**
                     * оьработка результата
                     */
                    Result(UserError.NO_ERROR,
                            composeAdapterData(measurementMapper.entityListToModelList(response.entityList)),
                            MeasurementStatus.NO_TASK)
                }.onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, composeAdapterData(emptyList()), MeasurementStatus.NO_TASK)
                }
    }
    /**
     * загрузка замеров со статусом задания на получения апп замеров
     */
    fun loadMeasurementsWithTaskStatus(workId: String): Single<Result> {
        return thingsWorxWorker.getWorkMeasurementsWithTaskStatus(workId)
                .map { response ->
                    /**
                     * обработка результата
                     */
                    val status = measurementStatusMapper.fromEntity(response.measurementsStatus)
                            ?: MeasurementStatus.NO_TASK
                    val measurements = measurementMapper.entityListToModelList(response.measurements)
                    Result(UserError.NO_ERROR, composeAdapterData(measurements), status)
                }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, AdapterData(), MeasurementStatus.NO_TASK)
                }
    }
    /**
     * загрузка замеров по заданию
     */
    fun loadMeasurementsByTask(workId: String, taskId: String, stage: Stage): Single<Result> {
        return thingsWorxWorker.checkMeasurementsReady(workId, taskId, stage.value)
                .map { response ->
                    /**
                     * обработка результата
                     */
                    val status = measurementStatusMapper.fromEntity(response.measurementsStatus)
                            ?: MeasurementStatus.NO_TASK
                    val measurements = measurementMapper.entityListToModelList(response.measurements)
                    Result(UserError.NO_ERROR, composeAdapterData(measurements), status)
                }
                .onErrorReturn {
                    /**
                     * отображение ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, AdapterData(), MeasurementStatus.NO_TASK)
                }
    }

    //Dedicated method used to have compose an adapter's data of irregular measurements data
    private fun composeAdapterData(measurements: List<Measurement>): AdapterData {
        val controlList = mutableListOf<MeasurementModel>()
        val beforeRepairList = mutableListOf<MeasurementModel>()
        val afterRepairList = mutableListOf<MeasurementModel>()

        //Distribute measurements by stage
        for (item in measurements) {
            when (item.stage) {
                Stage.CONTROL -> controlList.add(MeasurementModel(item))
                Stage.BEFORE_REPAIR -> beforeRepairList.add(MeasurementModel(item))
                Stage.AFTER_REPAIR -> afterRepairList.add(MeasurementModel(item))
            }
        }

        return AdapterData().apply {
            addMeasurements(Stage.CONTROL, controlList)
            addMeasurements(Stage.BEFORE_REPAIR, beforeRepairList)
            addMeasurements(Stage.AFTER_REPAIR, afterRepairList)
        }
    }
    /**
     * стандартный ответ метода
     */
    class Result(val userError: UserError, val items: AdapterData, val measurementStatus: MeasurementStatus) {
        val isSuccessful
            get() = userError === UserError.NO_ERROR
    }
}