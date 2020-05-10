package ru.digipeople.locotech.worker.api

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.model.WorkDetailEntity
import ru.digipeople.locotech.worker.api.model.response.*
import ru.digipeople.locotech.worker.model.MeasurementChangeRequest
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Класс [DefaultThingsWorxWorker] - реализация интерфейса [ThingsWorxWorker]
 *
 * @author Kashonkov Nikita
 */
class DefaultThingsWorxWorker(private val thingWorx: ThingWorx,
                              private val responseConverter: ResponseConverter) : ThingsWorxWorker {
    /**
     * Метод для авторизации
     */
    override fun auth(username: String, password: String, fbcToken: String): Single<AuthResponse> {
        return thingWorx.auth(username, password, fbcToken)
                .map { response ->
                    responseConverter.convertToEntity(response.result, AuthResponse::class.java)
                }
    }
    /**
     * Метод для запуска смены
     */
    override fun startShift(): Single<ResultResponse> {
        return thingWorx.startShift()
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для остановки смены
     */
    override fun stopShift(): Single<ResultResponse> {
        return thingWorx.stopShift()
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для получения списка работ
     */
    override fun getWorks(): Single<MyWorksResponse> {
        return thingWorx.getWorks()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, MyWorksResponse::class.java)
                }
    }
    /**
     * Метод для получения информации по работе
     */
    override fun getWorkDetail(workId: String): Single<WorkDetailEntity> {
        return thingWorx.getWorkDetail(workId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, WorkDetailEntity::class.java)
                }
    }
    /**
     * Метод для начала выполнения работы
     */
    override fun startWork(workId: String): Single<ResultResponse> {
        return thingWorx.startWork(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для постановки работы на паузу
     */
    override fun pauseWork(workId: String, reasonId: String): Single<ResultResponse> {
        return thingWorx.pauseWork(workId, reasonId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Отметить работу как выполненную
     */
    override fun doneWork(workId: String): Single<ResultResponse> {
        return thingWorx.doneWork(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для получения списка причин
     */
    override fun getReasons(): Single<PauseReasonResponse> {
        return thingWorx.getReasons()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, PauseReasonResponse::class.java)
                }
    }
    /**
     * Метод для добавления комментариев к работе
     */
    override fun addWorkComment(workId: String, text: String): Single<ResultResponse> {
        return thingWorx.editWorkComment(workId, text)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     */
    override fun loadTMCByWork(workId: String): Single<TMCInWorkResponse> {
        return thingWorx.getTMCInWork(workId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, TMCInWorkResponse::class.java)
                }
    }
    /**
     * Метод для запроса задания на проведение аппаратных замеров
     */
    override fun getMeasurementsTask(workId: String, sectionNumber: String, stage: Int): Single<MeasurementsTaskResponse> {
        return thingWorx.getMeasurementsTask(workId, sectionNumber, stage)
                .map { response ->
                    responseConverter.convertToEntity(response.result, MeasurementsTaskResponse::class.java)
                }
    }
    /**
     * Метод для проверки готовности аппаратных замеров
     */
    override fun checkMeasurementsReady(workId: String, taskId: String, stage: Int): Single<MeasurementsWithStatusResponse> {
        return thingWorx.checkMeasurementsReady(workId, taskId, stage)
                .map { response ->
                    responseConverter.convertToEntity(response.result, MeasurementsWithStatusResponse::class.java)
                }
    }
    /**
     * Метод для получения информации по замерам
     */
    override fun getWorkMeasurements(workId: String): Single<MeasurementsResponse> {
        return thingWorx.getWorkMeasurements(workId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, MeasurementsResponse::class.java)
                }
    }
    /**
     *  Метод отправки информации по замерам
     */
    override fun postChangeMeasurement(workId: String, requests: List<MeasurementChangeRequest>): Single<ResultResponse> {
        return Single.fromCallable {
            JsonConverter.convertToJson(requests)
        }.flatMap { thingWorx.postChangeMeasurement(workId, it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для получения чеклиста по работе
     */
    override fun getChecklist(workId: String): Single<ChecklistResponse> {
        return thingWorx.getChecklist(workId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, ChecklistResponse::class.java)
                }
    }
    /**
     * Метод для проставления признака проверки строки чеклиста
     */
    override fun postCheckChecklistItem(workId: String, checklistItemId: String): Single<ResultResponse> {
        return thingWorx.postCheckChecklistItem(workId, checklistItemId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для снятия признака проверки строки чеклиста
     */
    override fun postUncheckChecklistItem(workId: String, checklistItemId: String): Single<ResultResponse> {
        return thingWorx.postUncheckChecklistItem(workId, checklistItemId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
}