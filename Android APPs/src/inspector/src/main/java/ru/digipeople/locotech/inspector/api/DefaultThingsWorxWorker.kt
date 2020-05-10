package ru.digipeople.locotech.inspector.api

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.model.response.*
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.thingworx.model.IDListEntity
import ru.digipeople.thingworx.model.SignersCategoryEntity
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 *  Класс [DefaultThingsWorxWorker] - реализация интерфейса [ThingsWorxWorker]
 *
 * @author Kashonkov Nikita
 */
class DefaultThingsWorxWorker(private val thingWorx: ThingWorx,
                              private val responseConverter: ResponseConverter) : ThingsWorxWorker {
    /**
     * Метод для авторизации
     *
     */
    override fun auth(username: String, password: String) = thingWorx.auth(username, password)
            .map { response ->
                responseConverter.convertToEntity(response.result, AuthResponse::class.java)
            }
    /**
     * Получает список всего оборудования, находящегося на ремонте
     */
    override fun getEquipment(): Single<EquipmentResponse> = thingWorx.getEquipment()
            .map { response ->
                responseConverter.convertToEntityList(response.result, EquipmentResponse::class.java)
            }
    /**
     * Метод устанавливает выбранное оборудование в качестве текущего
     */
    override fun setSelectedSection(equipmentId: String): Single<ResultResponse> = thingWorx.setSelectedSection(equipmentId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод устанавливает выбранное оборудование в качестве текущего
     */
    override fun setSelectedEquipment(equipmentId: String): Single<ResultResponse> = thingWorx.setSelectedEquipment(equipmentId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для создания кастомного замечания
     */
    override fun createRemark(remarkName: String): Single<CreateRemarkResponse> = thingWorx.createRemark(remarkName)
            .map { response ->
                responseConverter.convertToEntity(response.result, CreateRemarkResponse::class.java)
            }
    /**
     * Метод для выбора замечания
     */
    override fun addRemark(remarkId: String): Single<CreateRemarkResponse> = thingWorx.addRemark(remarkId)
            .map { response ->
                responseConverter.convertToEntity(response.result, CreateRemarkResponse::class.java)
            }
    /**
     * Метод для получения списка возможных замечаний
     */
    override fun getRemarksFromCatalog(): Single<RemarkFromCatalogResponse> = thingWorx.getRemarksFromCatalog()
            .map { response ->
                responseConverter.convertToEntityList(response.result, RemarkFromCatalogResponse::class.java)
            }
    /**
     * Метод для добавления комментариев к работе
     */
    override fun addWorkComent(workId: String, text: String): Single<ResultResponse> = thingWorx.editWorkComment(workId, text)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для добавления комментариев к замечанию
     */
    override fun addRemarkComent(remarkId: String, text: String): Single<ResultResponse> = thingWorx.editRemarkComment(remarkId, text)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для получния списка возможных подписантов
     */
    override fun getSignerList(groupId: String): Single<SignerResponse> = thingWorx.getSigners(groupId)
            .map { response ->
                responseConverter.convertToEntityList(response.result, SignerResponse::class.java)
            }
    /**
     * Метод для получения цикловых работ
     */
    override fun getCyclicWorks(): Single<CyclicWorkResponse> = thingWorx.getCyclicWorks()
            .map { response ->
                responseConverter.convertToEntity(response.result, CyclicWorkResponse::class.java)
            }
    /**
     * Метод для получения замечаний ОТК
     */
    override fun getOTKRemarks(): Single<RemarkSldResponse> = thingWorx.getOTKRemarks()
            .map { response ->
                responseConverter.convertToEntity(response.result, RemarkSldResponse::class.java)
            }
    /**
     * Метод для получения замечаний РЖД
     */
    override fun getRzdRemarks(): Single<RemarkRzdResponse> = thingWorx.getRzdRemarks()
            .map { response ->
                responseConverter.convertToEntity(response.result, RemarkRzdResponse::class.java)
            }
    /**
     * Метод для проставления признака принятия работы/замечания сотрудником СЛД группе работ
     */
    override fun acceptSLD(workIds: List<String>): Single<ResultResponse> = Single.fromCallable {
        val entity = IDListEntity(workIds)
        JsonConverter.convertToJson(entity)
    }
            .flatMap { thingWorx.acceptGroupSLD(it) }
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для снятия признака принятия работы/замечания сотрудником СЛД
     */
    override fun declineSLD(workId: String): Single<ResultResponse> = thingWorx.declineSLD(workId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для проставления признака принятия работы/замечания сотрудником РЖД
     */
    override fun acceptRZD(workIds: List<String>): Single<ResultResponse> = Single
            .fromCallable {
                val entity = IDListEntity(workIds)
                JsonConverter.convertToJson(entity)
            }
            .flatMap { thingWorx.acceptGroupRZD(it) }
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для получения документов
     */
    override fun getDocuments(): Single<DocumentResponse> {
        return thingWorx.getDocuments()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, DocumentResponse::class.java)
                }
    }
    /**
     * Метод для снятия признака принятия работы/замечания сотрудником РЖД
     */
    override fun declineRZD(workId: String): Single<ResultResponse> = thingWorx.declineRZD(workId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для получения сводной инофрмации
     */
    override fun getSummary(): Single<SummaryResponse> = thingWorx.getSummary()
            .map { response ->
                responseConverter.convertToEntityList(response.result, SummaryResponse::class.java)
            }
    /**
     * Метод для получения причин удаления замечания
     */
    override fun getDeclinedReasons(): Single<DeclineReasonResponse> = thingWorx.getDeclineReasons()
            .map { response ->
                responseConverter.convertToEntityList(response.result, DeclineReasonResponse::class.java)
            }
    /**
     * Список должностных лиц
     */
    override fun getExecutiveList(executiveNameSubstring: String): Single<SignerListResponse> = thingWorx.getExecutiveList(executiveNameSubstring)
            .map { response ->
                responseConverter.convertToEntityList(response.result, SignerListResponse::class.java)
            }
    /**
     * Метод для получения контрольных точек
     */
    override fun getControlPoint(workId: String): Single<ControlPointsResponse> = thingWorx.getControlPoints(workId)
            .map { response ->
                responseConverter.convertToEntity(response.result, ControlPointsResponse::class.java)
            }
    /**
     * Список документов для печати
     */
    override fun documentsForPrinting() = thingWorx.documentsForPrinting()
            .map { response ->
                responseConverter.convertToEntityList(response.result, DocumentsForPrintingResponse::class.java)
            }
    /**
     * Список категорий подписантов для печати
     */
    override fun documentsSigners() = thingWorx.documentsSigners()
            .map { response ->
                responseConverter.convertToEntityList(response.result, DocumentsSignersResponse::class.java)
            }
    /**
     * Метод для получения чеклиста
     */
    override fun getChecklist(): Single<ChecklistResponse> = thingWorx.getChecklist()
            .map { response ->
                responseConverter.convertToEntityList(response.result, ChecklistResponse::class.java)
            }
    /**
     * Метод для проставления признака принятия Контрольно-Сервисной операции(КСО) сотрудником СЛД
     */
    override fun acceptCsoSLD(csoId: String) = thingWorx.acceptCsoSLD(csoId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для снятия признака принятия ксо сотрудником СЛД
     */
    override fun declineCsoSLD(csoId: String) = thingWorx.declineCsoSLD(csoId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для проставления признака принятия Контрольно-Сервисной операции(КСО) сотрудником РЖД
     */
    override fun acceptCsoRZD(csoId: String) = thingWorx.acceptCsoRzd(csoId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для снятия признака принятия ксо сотрудником РЖД
     */
    override fun declineCsoRZD(csoId: String) = thingWorx.declineCsoRzd(csoId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для удаления замечания
     */
    override fun revokeRemark(remarkId: String, reasonId: String) = thingWorx.revokeRemarkWork(remarkId, reasonId)
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для добавления комментариев к ксо
     */
    override fun editCsoComment(csoId: String, text: String) = thingWorx.editCsoComment(csoId, text)
            .map { response ->
                responseConverter.convertResultResponse(response.result)
            }
    /**
     * Метод для вызова представителя РЖД
     */
    override fun notifyRzd() = thingWorx.notifyRzd()
            .map { response ->
                responseConverter.convertResultResponse(response.result)
            }
    /**
     * Принять чек-лист
     */
    override fun acceptInspectionRzd(): Single<ResultResponse> = thingWorx.acceptInspectionRzd()
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }

    override fun getNotAcceptedCount(): Single<ResultResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
     * Метод для формирования указанных документов для печати по указанному идентификатору секции с заданным списком подписантов
     */
    override fun printProtocol(emailAcceptor: String, documentList: List<String>, signersCategoryList: List<SignersCategoryEntity>): Single<ResultResponse> {
        return Single
                .fromCallable {
                    val json1 = JsonConverter.convertToJson(documentList)
                    val json2 = JsonConverter.convertToJson(signersCategoryList)
                    json1 to json2
                }
                .flatMap { thingWorx.printProtocol(emailAcceptor, it.first, it.second) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для установки электронной почты пользователя
     */
    override fun setEmailForPrint(email: String): Single<ResultResponse> {
        return thingWorx.setEmailForPrint(email)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
}