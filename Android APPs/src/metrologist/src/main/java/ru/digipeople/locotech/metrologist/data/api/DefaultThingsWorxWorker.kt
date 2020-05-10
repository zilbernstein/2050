package ru.digipeople.locotech.metrologist.data.api

import io.reactivex.Completable
import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementInfoEntity
import ru.digipeople.locotech.metrologist.data.api.response.*
import ru.digipeople.locotech.metrologist.data.api.entity.WheelParamsValueEntity
import ru.digipeople.locotech.metrologist.data.api.mapper.measurementMapper
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter

/**
 *  Класс [DefaultThingsWorxWorker] - реализация интерфейса [ThingsWorxWorker]
 *  
 * @author Kashonkov Nikita
 */
class DefaultThingsWorxWorker(private val thingWorx: ThingWorx,
                              private val responseConverter: ResponseConverter) : ThingsWorxWorker {
    /**
     * Метод для авторизации
     */
    override fun auth(username: String, password: String): Single<AuthResponse> = thingWorx.auth(username, password)
            .map { response ->
                responseConverter.convertToEntity(response.result, AuthResponse::class.java)
            }
    /**
     * Метод для получения видов замеров
     */
    override fun getMeasurementCategories(): Single<MeasurementCategoriesResponse> {
        return thingWorx.getMeasurementCategories()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, MeasurementCategoriesResponse::class.java)
                }
    }
    /**
     * Метод для получения общую информацию по замеру
     */
    override fun getMeasurementInfo(categoryId: String): Single<MeasurementInfoEntity> {
        return thingWorx.getMeasurementInfo(categoryId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, MeasurementInfoEntity::class.java)
                }
    }
    /**
     * Сохранить общую информацию по замеру
     */
    override fun setMeasurementInfo(measurement: Measurement): Single<MeasurementInfoEntity> {
        return Single
                .defer {
                    val measurementEntity = measurementMapper.toEntity(measurement)
                    thingWorx.setMeasurementInfo(
                            JsonConverter.convertToJson(measurementEntity)
                    )
                }
                .map { response ->
                    responseConverter.convertToEntity(response.result, MeasurementInfoEntity::class.java)
                }
    }
    /**
     * Сохранить (проектные) данные по КП
     */
    override fun setPairParamsValues(pairId: String, wheelParamsValues: List<WheelParamsValueEntity>): Single<WheelPairsResponse> {
        return thingWorx.setPairParamsValues(pairId,
                JsonConverter.convertToJson(wheelParamsValues)
        )
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WheelPairsResponse::class.java)
                }
    }
    /**
     * Получить справочник причин обточки
     */
    override fun getProcessingReasons(): Single<ProcessingReasonsResponse> {
        return thingWorx.getProcessingReasons()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, ProcessingReasonsResponse::class.java)
                }
    }
    /**
     * Установить причину обточки
     */
    override fun setProcessingReason(reasonId: String, wheelPairId: String, measurementId: String): Completable {
        return thingWorx.setProcessingReason(reasonId, wheelPairId, measurementId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
                .toCompletable()
    }
    /**
     * Метод для получения списка секций
     */
    override fun getSections(): Single<SectionsResponse> = thingWorx.getSections()
            .map { response ->
                responseConverter.convertToEntityList(response.result, SectionsResponse::class.java)
            }
    /**
     * Метод для получения списка измерений профилометра
     */
    override fun getProfilometerMeasurements(): Single<MeasurementResponse> = thingWorx.getProfilometerMeasurements()
            .map { response ->
                responseConverter.convertToEntityList(response.result, MeasurementResponse::class.java)
            }
    /**
     * Загрузка данных профилометра в существующий замер
     */
    override fun loadProfilometerMeasurement(acceptorId: String, donorId: String): Single<MeasurementInfoEntity> = thingWorx.loadProfilometerMeasurement(acceptorId, donorId)
            .map { response ->
                responseConverter.convertToEntity(response.result, MeasurementInfoEntity::class.java)
            }
    /**
     * Получить список документов-отчетов
     */
    override fun getReports(): Single<ReportsResponse> = thingWorx.getReports()
            .map { response ->
                responseConverter.convertToEntityList(response.result, ReportsResponse::class.java)
            }
    /**
     * Выбрать секцию / установить как текущую
     */
    override fun postSelectSection(sectionId: String): Completable {
        return thingWorx.postSelectSection(sectionId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
                .toCompletable()
    }
    /**
     * Создать задание на отправку отчета
     */
    override fun taskSendReport(reportId: String, emailAddress: String): Completable {
        return thingWorx.taskSendReport(reportId, emailAddress)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
                .toCompletable()
    }
    /**
     * Подтвердить создание замера в АСУ СГ и создание замечаний
     */
    override fun postCompleteMeasurement(measurementId: String): Completable {
        return thingWorx.postCompleteMeasurement(measurementId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
                .toCompletable()
    }
    /**
     * Получить детальную информацию по идентификатору замера
     */
    override fun getWheelPairs(measurementId: String): Single<WheelPairsResponse> {
        return thingWorx.getWheelPairs(measurementId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WheelPairsResponse::class.java)
                }
    }
    /**
     * Получить информацию по замеру (включая замечания) перед подтверждением
     */
    override fun getMeasurementInfoBeforeComplete(measurementId: String): Single<MeasurementInfoBeforeCompleteResponse> {
        return thingWorx.getMeasurementInfoBeforeComplete(measurementId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, MeasurementInfoBeforeCompleteResponse::class.java)
                }
    }
}