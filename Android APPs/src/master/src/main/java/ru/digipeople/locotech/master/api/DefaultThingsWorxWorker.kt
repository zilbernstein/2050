package ru.digipeople.locotech.master.api

import io.reactivex.Single
import ru.digipeople.locotech.master.api.model.WorksAndBrigadesEntity
import ru.digipeople.locotech.master.api.model.request.OrderToRepairRequest
import ru.digipeople.locotech.master.api.model.request.WorkerPresenceRequest
import ru.digipeople.locotech.master.api.model.response.*
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.thingworx.model.IDListEntity
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 *  Класс [DefaultThingsWorxWorker] - реализация интерфейса [ThingsWorxWorker]
 *
 * @author Kashonkov Nikita
 */
class DefaultThingsWorxWorker(private val thingWorx: ThingWorx,
                              private val responseConverter: ResponseConverter) : ThingsWorxWorker {
    /**
     * Получает список всего оборудования, находящегося на ремонте
     */
    override fun getEquipment(): Single<EquipmentResponse> {
        return thingWorx.getEquipment()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, EquipmentResponse::class.java)
                }
    }
    /**
     * Метод для запроса на получение ремкомплекта
     */
    override fun orderToRepair(orderToRepairRequest: OrderToRepairRequest): Single<OrderToRepairResponse> {
        return thingWorx.orderToRepair(orderToRepairRequest)
                .map { response ->
                    responseConverter.convertToEntity(response.result, OrderToRepairResponse::class.java)
                }
    }
    /**
     * Получает список всех замечаний с работами для оборудования
     */
    override fun getRemarkByEquipment(): Single<EquipmentRemarkResponse> {
        return thingWorx.getRemarkbyEquipment()
                .map { response ->
                    responseConverter.convertToEntity(response.result, EquipmentRemarkResponse::class.java)
                }
    }
    /**
     * Получает список всех работ по оборудованию
     */
    override fun getWorksByEquipment(): Single<WorkResponse> {
        return thingWorx.getWorksByEquipment()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }
    }
    /**
     * Получает список работ для согласования и запуска
     */
    override fun getWorksForStartAndApprove(): Single<WorkResponse> {
        return thingWorx.getWorksForStartAndApprove()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }
    }
    /**
     * Метод для получения списка работ, которые можно отправить на согласования
     */
    override fun getWorksReadyForApprove(): Single<WorkResponse> {
        return thingWorx.getWorksReadyForApprove()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }
    }
    /**
     * Метод для получения списка работ, которые можно запустить
     */
    override fun getWorksReadyForStart(): Single<WorkResponse> {
        return thingWorx.getWorksReadyForStart()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }
    }
    /**
     * Метод для получения списка работ, которые можно принять
     */
    override fun getWorksReadyForMasterAccept(): Single<WorkResponse> {
        return thingWorx.getWorksReadyForMasterAccept()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }
    }
    /**
     * Метод для приостановки работы
     */
    override fun stopWork(workId: String): Single<ResultResponse> {
        return thingWorx.stopWork(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для принятия работы
     */
    override fun acceptWork(workId: String): Single<ResultResponse> {
        return thingWorx.acceptWork(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }

    /**
     * Метод для приемки всех работ
     */
    override fun acceptWorks(workIdList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workIdList)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.acceptWorks(it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для запуска работы
     */
    override fun startWork(workId: String): Single<ResultResponse> {
        return thingWorx.startWork(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для запуска списка работ
     */
    override fun startWork(workIdList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workIdList)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.startWorks(it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для согласования списка работ
     */
    override fun approveWork(workIdList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workIdList)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.approveWorks(it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для добавления комментариев к работе
     */
    override fun addWorkComent(workId: String, text: String): Single<ResultResponse> {
        return thingWorx.editWorkComment(workId, text)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для добавления комментариев к замечанию
     */
    override fun addRemarkComent(remarkId: String, text: String): Single<ResultResponse> {
        return thingWorx.editRemarkComment(remarkId, text)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для получния списка возможных исполнителей для данной работы
     */
    override fun getPerformersList(workId: String): Single<PerformerResponse> {
        return thingWorx.getPerformersList(workId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, PerformerResponse::class.java)
                }
    }
    /**
     * Метод для получния списка возможных исполнителей для данной работы
     */
    override fun setPerformers(workId: String, performersIdList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(performersIdList)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.setPerformers(workId, it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для получения списка возможных замечаний
     */
    override fun getRemarksFromCatalog(): Single<RemarkFromCatalogResponse> {
        return thingWorx.getRemarksFromCatalog()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, RemarkFromCatalogResponse::class.java)
                }
    }
    /**
     * Метод для получения списка возможных работ
     */
    override fun getWorksFromCatalog(remarkId: String, queryStr: String): Single<WorkFromCatalogResponse> {
        return thingWorx.getWorksFromCatalog(remarkId, queryStr)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkFromCatalogResponse::class.java)
                }
    }
    /**
     * Метод для получения списка работ по замеачанию
     */
    override fun getWorksFromRemark(remarkId: String): Single<WorkResponse> {
        return thingWorx.getWorksFromRemark(remarkId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }
    }
    /**
     * Метод для добавления списка работ
     */
    override fun addWorks(remarkId: String, workIds: Collection<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workIds)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.addWorks(remarkId, it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }

    /**
     * Метод для добавления списка работ
     */
    override fun addWorks(remarkId: String, workId: String, repeatsCount: Int): Single<ResultResponse> {
        return thingWorx.addWork(remarkId, workId, repeatsCount)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для создания кастомного замечания
     */
    override fun createRemark(remarkName: String): Single<CreateRemarkResponse> {
        return thingWorx.createRemark(remarkName)
                .map { response ->
                    responseConverter.convertToEntity(response.result, CreateRemarkResponse::class.java)
                }
    }
    /**
     * Метод для выбора замечания
     */
    override fun chooseRemark(choiceId: String): Single<CreateRemarkResponse> {
        return thingWorx.chooseRemark(choiceId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, CreateRemarkResponse::class.java)
                }
    }
    /**
     * Метод для добавления замечания
     */
    override fun addRemark(remarkId: String): Single<ResultResponse> {
        return thingWorx.addRemark(remarkId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для удаления замечания
     */
    override fun deleteRemark(remarkId: String): Single<ResultResponse> {
        return thingWorx.deleteRemark(remarkId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для удаления работы
     */
    override fun deleteWork(workId: String): Single<ResultResponse> {
        return thingWorx.deleteWork(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Получает список работ по категории срочно
     */
    override fun getUrgent(): Single<WorkResponse> {
        return thingWorx.getUrgent()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WorkResponse::class.java)
                }

    }
    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     */
    override fun getTMCInWork(workId: String): Single<TMCInWorkResponse> {
        return thingWorx.getTMCInWork(workId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, TMCInWorkResponse::class.java)
                }
    }
    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     */
    override fun getTMC(queryStr: String, stockAvailable: Boolean): Single<TMCResponse> {
        return thingWorx.getTMC(queryStr, stockAvailable)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, TMCResponse::class.java)
                }
    }
    /**
     * Удалить ТМЦ из работы
     */
    override fun deleteTMC(idWork: String, idTMC: String): Single<ResultResponse> {
        return thingWorx.deleteTMC(idWork, idTMC)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Редактировать количество ТМЦ в работе
     */
    override fun editTMCInWork(idWork: String, idTMC: String, tmcAmount: Double): Single<ResultResponse> {
        return thingWorx.editTMCInWork(idWork, idTMC, tmcAmount)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Редактировать количество ТМЦ для списания
     */
    override fun editTMCForWriteOff(idWork: String, idTMC: String, tmcAmount: Double): Single<ResultResponse> {
        return thingWorx.editTMCForWriteOff(idWork, idTMC, tmcAmount)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Получает список работ с ТМЦ для списания
     */
    override fun getTmcForWriteOff(workIds: Collection<String>): Single<WriteOffTmcResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(workIds)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.getTmcForWriteOff(it) }
                .map { response ->
                    responseConverter.convertToEntityList(response.result, WriteOffTmcResponse::class.java)
                }
    }
    /**
     * Метод разделяет работу на наряды
     */
    override fun divideWork(workId: String, dividedTime: Long): Single<ResultResponse> {
        return thingWorx.divideWork(workId, dividedTime)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод осуществляет частичную приемку работы
     */
    override fun partlyAcceptWork(workId: String, dividedTime: Long): Single<ResultResponse> {
        return thingWorx.partlyAcceptWork(workId, dividedTime)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод возвращает список работ без исполнителей и список бригад
     */
    override fun getWorkAndBrigadesForAssignment(): Single<WorksAndBrigadesEntity> {
        return thingWorx.getWorkAndBrigadesForAssignment()
                .map { response ->
                    responseConverter.convertToEntity(response.result, WorksAndBrigadesEntity::class.java)
                }
    }
    /**
     * Метод назначает группу исполнителей для группы работ
     */
    override fun assignGroupWorkersToWorks(workList: Collection<String>, workerList: Collection<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val workJson = JsonConverter.convertToJson(workList)
            val workerJson = JsonConverter.convertToJson(workerList)
            Pair(workJson, workerJson)
        }.flatMap {
            thingWorx.assignGroupWorkersToWork(it.first, it.second)
                    .map { response ->
                        responseConverter.convertResultEntity(response.result)
                    }
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
     * Метод для получения информации по замерам
     */
    override fun getWorkMeasurementsWithTaskStatus(workId: String): Single<MeasurementsWithStatusResponse> {
        return thingWorx.getWorkMeasurementsWithTaskStatus(workId)
                .map { response ->
                    responseConverter.convertToEntity(response.result, MeasurementsWithStatusResponse::class.java)
                }
    }
    /**
     * Метод для отправки информации по замерам
     */
    override fun editMeasure(workId: String, measure: IndicatorResponse): Single<ResultResponse> {
        return Single.fromCallable {
            JsonConverter.convertToJson(measure)
        }.flatMap {
            thingWorx.editMeasure(workId, it)
                    .map { response ->
                        responseConverter.convertResultEntity(response.result)
                    }
        }
    }
    /**
     * Метод для первода работы в статус "Обходное решение" (WORKAROUND)
     */
    override fun workaround(workId: String): Single<ResultResponse> {
        return thingWorx.workaround(workId)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для полчения детальных данных о явке сотрудников
     */
    override fun brigadePresence(): Single<BrigadePresenceResponse> {
        return thingWorx.brigadePresence()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, BrigadePresenceResponse::class.java)
                }
    }
    /**
     * Метод для добавления изменения явки по сотруднику
     */
    override fun changeWorkerPresence(presenceRequest: WorkerPresenceRequest): Single<ChangePresenceResponse> {
        return thingWorx.changeWorkerPresence(presenceRequest)
                .map { response ->
                    responseConverter.convertToEntity(response.result, ChangePresenceResponse::class.java)
                }
    }
    /**
     * Метод для получения список шаблонов для данного мастера на текущей секции
     */
    override fun assignmentTemplates(): Single<AssignmentTemplatesResponse> {
        return thingWorx.assignmentTemplates()
                .map { response ->
                    responseConverter.convertToEntityList(response.result, AssignmentTemplatesResponse::class.java)
                }
    }
    /**
     * Метод для выбора шаблона
     */
    override fun setAssignmentTemplate(id: String, date: Long, workNight: Boolean): Single<ResultResponse> {
        return thingWorx.setAssignmentTemplate(id, date, workNight)
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
}