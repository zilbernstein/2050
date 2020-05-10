package ru.digipeople.locotech.metrologist.data.api

import io.reactivex.Completable
import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.entity.MeasurementInfoEntity
import ru.digipeople.locotech.metrologist.data.api.entity.WheelParamsValueEntity
import ru.digipeople.locotech.metrologist.data.api.response.*
import ru.digipeople.locotech.metrologist.data.model.Measurement

/**
 * Интерфейс для [ThingsWorxWorker]
 *
 * @author Kashonkov Nikita
 */
interface ThingsWorxWorker {
    /**
     * Метод авторизации
     */
    fun auth(username: String, password: String): Single<AuthResponse>

    /**
     * Метод получения видов замеров
     */
    fun getMeasurementCategories(): Single<MeasurementCategoriesResponse>

    /**
     * Получить общую информацию по замеру
     */
    fun getMeasurementInfo(categoryId: String): Single<MeasurementInfoEntity>

    /**
     * Получает список секций
     */
    fun getSections(): Single<SectionsResponse>

    /**
     * Получает список замеров в профилометрах
     */
    fun getProfilometerMeasurements(): Single<MeasurementResponse>

    /**
     * Загрузить данные профилометра в существующий замер
     */
    fun loadProfilometerMeasurement(acceprotId: String, donorId: String): Single<MeasurementInfoEntity>

    /**
     * Сохранить общую информацию по замеру
     */
    fun setMeasurementInfo(measurement: Measurement): Single<MeasurementInfoEntity>

    fun getProcessingReasons(): Single<ProcessingReasonsResponse>

    fun setProcessingReason(reasonId: String, wheelPairId: String, measurementId: String): Completable
    /**
     * Получить список документов-отчетов
     */
    fun getReports(): Single<ReportsResponse>

    /**
     * Выбрать секцию / установить как текущую
     */
    fun postSelectSection(sectionId: String): Completable

    /**
     * Подтвердить создание замера в АСУ СГ и создание замечаний
     */
    fun postCompleteMeasurement(measurementId: String): Completable

    /**
     * Создать задание на отправку отчета
     */
    fun taskSendReport(reportId: String, emailAddress: String): Completable

    /**
     * Получить детальную информацию по идентификатору замера
     */
    fun getWheelPairs(measurementId: String): Single<WheelPairsResponse>

    /**
     * Получить информацию по замеру (включая замечания) перед подтверждением
     */
    fun getMeasurementInfoBeforeComplete(measurementId: String): Single<MeasurementInfoBeforeCompleteResponse>

    /**
     * Сохранить (проектные) данные по КП
     */
    fun setPairParamsValues(pairId: String, wheelParamsValues: List<WheelParamsValueEntity>): Single<WheelPairsResponse>
}