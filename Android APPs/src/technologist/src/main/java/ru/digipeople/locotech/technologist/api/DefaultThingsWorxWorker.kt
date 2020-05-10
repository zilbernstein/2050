package ru.digipeople.locotech.technologist.api

import io.reactivex.Single
import ru.digipeople.locotech.technologist.api.response.AuthResponse
import ru.digipeople.locotech.technologist.api.response.EquipmentResponse
import ru.digipeople.locotech.technologist.api.response.WorkDetailResponse
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.thingworx.model.IDListEntity
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
     *
     */
    override fun auth(username: String, password: String, fbcToken: String): Single<AuthResponse> {
        return thingWorx.auth(username, password, fbcToken)
                .map { response ->
                    responseConverter.convertToEntity(response.result, AuthResponse::class.java)
                }
    }
    /**
     * Получает список всех замечаний и работ
     */
    override fun getRemarkAndWork(): Single<EquipmentResponse> {
        return thingWorx.getRemarkAndWork()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, EquipmentResponse::class.java)
                }
    }
    /**
     * Получает список деталки работы
     */
    override fun getWorkDetail(workId: String): Single<WorkDetailResponse> {
        return thingWorx.getWorkDetail(workId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, WorkDetailResponse::class.java)
                }
    }
    /**
     * Выбирает работу для согласования
     */
    override fun setWorkCheck(workList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workList)
            JsonConverter.convertToJson(entity)
        }.flatMap { thingWorx.setWorkCheck(it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Выбирает работу для снятия согласования
     */
    override fun setWorkUncheck(workList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workList)
            JsonConverter.convertToJson(entity)
        }.flatMap { thingWorx.setWorkUncheck(it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Завершает согласование по замечанию
     */
    override fun approveWorks(remarkId: String, workIds: List<String>): Single<ResultResponse> {
        return thingWorx.approveWorks(remarkId, workIds)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Отклоняет согласование по замечанию
     */
    override fun rejectWorks(remarkId: String, workIds: List<String>): Single<ResultResponse> {
        return thingWorx.rejectWorks(remarkId, workIds)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Меняет комментарий к выбранной работе
     */
    override fun setWorkComment(idWork: String, text: String): Single<ResultResponse> {
        return thingWorx.setWorkComment(idWork, text)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
}