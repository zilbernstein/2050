package ru.digipeople.locotech.metrologist.data.api

import android.content.Context
import io.reactivex.Single
import ru.digipeople.locotech.core.R
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.helper.AuthorizationMistakeException
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Класс [ThingWorx] - Api МП Выпуск на линию
 *
 * @author Michael Solenov
 */
@Singleton
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx, private val context: Context) {
    /**
     * Метод для авторизации
     */
    fun auth(username: String, password: String): Single<ApiResponse> {
        return baseThingWorx.connectRx(username, password)
                .doOnSuccess { connected ->
                    if (!connected) {
                        throw AuthorizationMistakeException(context.getString(R.string.connection_error))
                    }
                }
                .flatMap {
                    requestThingWorxRx("signin", emptyMap())
                }
    }

    /**
     * Метод для получения видов замеров
     */
    fun getMeasurementCategories(): Single<ApiResponse> {
        return requestThingWorxRx("measurement_categories", emptyMap())
    }

    /**
     * Метод для получения общую информацию по замеру
     */
    fun getMeasurementInfo(categoryId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("category_id", categoryId)
            map
        }.flatMap { requestThingWorxRx("measurement_info", it) }
    }

    /**
     * Сохранить общую информацию по замеру
     */
    fun setMeasurementInfo(measurement: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("measurement", measurement)
            map
        }.flatMap { requestThingWorxRx("set_measurement_info", it) }
    }

    /**
     * Метод для получения списка секций
     */
    fun getSections(): Single<ApiResponse> {
        return requestThingWorxRx("sections", emptyMap())
    }

    /**
     * Метод для получения списка измерений профилометра
     */
    fun getProfilometerMeasurements(): Single<ApiResponse> {
        return requestThingWorxRx("profiler_measurements", emptyMap())
    }

    /**
     * Загрузка данных профилометра в существующий замер
     */
    fun loadProfilometerMeasurement(acceptorId: String, donorId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("acceptor_measurement_id", acceptorId)
            map.put("donor_measurement_id", donorId)
            map
        }.flatMap { requestThingWorxRx("load_profiler_measurement", it) }
    }

    /**
     * Получить справочник причин обточки
     */
    fun getProcessingReasons(): Single<ApiResponse> {
        return requestThingWorxRx("processing_reasons", emptyMap())
    }

    /**
     * Установить причину обточки
     */
    fun setProcessingReason(reasonId: String, wheelPairId: String, measurementId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("measurements_id", measurementId)
            map.put("wheel_pair_id", wheelPairId)
            map.put("reason_id", reasonId)
            map
        }.flatMap { requestThingWorxRx("set_processing_reason", it) }
    }

    /**
     * Получить список документов-отчетов
     */
    fun getReports(): Single<ApiResponse> {
        return requestThingWorxRx("reports", emptyMap())
    }

    /**
     * Выбрать секцию / установить как текущую
     */
    fun postSelectSection(sectionId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("section_id", sectionId)
            map
        }.flatMap { requestThingWorxRx("select_section", it) }
    }

    /**
     * Создать задание на отправку отчета
     */
    fun taskSendReport(reportId: String, emailAddress: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("report_id", reportId)
            map.put("email", emailAddress)
            map
        }.flatMap { requestThingWorxRx("task_send_report", it) }
    }

    /**
     * Подтвердить создание замера в АСУ СГ и создание замечаний
     */
    fun postCompleteMeasurement(measurementId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("measurement_id", measurementId)
            map
        }.flatMap { requestThingWorxRx("complete_measurement", it) }
    }

    /**
     * Получить детальную информацию по идентификатору замера
     */
    fun getWheelPairs(measurementId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("measurement_id", measurementId)
            map
        }.flatMap { requestThingWorxRx("wheel_pairs", it) }
    }

    /**
     * Получить информацию по замеру (включая замечания) перед подтверждением
     */
    fun getMeasurementInfoBeforeComplete(measurementId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("measurement_id", measurementId)
            map
        }.flatMap { requestThingWorxRx("measurement_info_before_complete", it) }
    }

    /**
     * Сохранить (проектные) данные по КП
     */
    fun setPairParamsValues(pairId: String, wheelParamsValue: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("pair_id", pairId)
            map.put("wheel_params_values", wheelParamsValue)
            map
        }.flatMap { requestThingWorxRx("set_pair_params_values", it) }
    }


    private fun requestThingWorxRx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }

    private suspend fun requestThingWorx(serviceName: String, params: Map<String, Any>): ApiResponse {
        return baseThingWorx.requestThingWorx(serviceName, params)
    }

}