package ru.digipeople.locotech.master.api

import android.content.Context
import io.reactivex.Single
import ru.digipeople.locotech.master.api.model.request.OrderToRepairRequest
import ru.digipeople.locotech.master.api.model.request.WorkerPresenceRequest
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс [ThingWorx] - Api МП Выпуск на линию
 *
 * @author Kashonkov Nikita
 */
@Singleton
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx, private val context: Context) {
    /**
     * Получает список всего оборудования, находящегося на ремонте
     *
     * @return ApiResponse со спсиском оборудования
     */
    fun getEquipment(): Single<ApiResponse> {
        return requestThingWorx("equipements", emptyMap())
    }

    /**
     * Получает список всех замечаний с работами для оборудования
     *
     * @return InfoTable со спсиском оборудования
     */
    fun getRemarkbyEquipment(): Single<ApiResponse> {
        return requestThingWorx("all_remark_work", emptyMap())
    }

    /**
     * Получает список всех работ по оборудованию
     *
     * @return ApiResponse со списком работ
     */
    fun getWorksByEquipment(): Single<ApiResponse> {
        return requestThingWorx("works_execution", emptyMap())
    }

    /**
     * Получает список работ для согласования и запуска
     *
     * @return ApiResponse со списком работ
     */
    fun getWorksForStartAndApprove(): Single<ApiResponse> {
        return requestThingWorx("work_for_approve", emptyMap())
    }

    /**
     * Метод для получения списка работ, которые можно отправить на согласования
     *
     * @return ApiResponse со списком работ
     */
    fun getWorksReadyForApprove(): Single<ApiResponse> {
        return requestThingWorx("worklist_readyto_approve", emptyMap())
    }

    /**
     * Метод для получения списка работ, которые можно запустить
     *
     * @return ApiResponse со списком работ
     */
    fun getWorksReadyForStart(): Single<ApiResponse> {
        return requestThingWorx("worklist_readyto_start", emptyMap())
    }


    /**
     * Метод для получения списка работ, которые можно принять
     *
     * @return ApiResponse со списком работ
     */
    fun getWorksReadyForMasterAccept(): Single<ApiResponse> {
        return requestThingWorx("worklist_readyto_accept", emptyMap())
    }

    /**
     * Метод для приостановки работы
     *
     * @param workId - идентификатор работы
     * @return ApiResponse с булевым флагом была-ли приостановлена работа
     */
    fun stopWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("work_stop", it) }
    }

    /**
     * Метод для принятия работы
     *
     * @param workId - идентификатор работы
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun acceptWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("work_accept_master", it) }
    }

    /**
     * Метод для приемки всех работ
     *
     * @param workIdList - списко идентификаторов работ
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun acceptWorks(workIdListJSON: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_works", workIdListJSON)
            map
        }.flatMap { requestThingWorx("works_accept_master", it) }
    }

    /**
     * Метод для запуска работы
     *
     * @param workId - идентификатор работы
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun startWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("work_to_task", it) }
    }

    /**
     * Метод для запуска списка работ
     *
     * @param workIdList - списко идентификаторов работ
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun startWorks(workIdListJSON: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_works", workIdListJSON)
            map
        }.flatMap { requestThingWorx("works_to_task", it) }
    }

    /**
     * Метод для согласования списка работ
     *
     * @param workId - идентификатор работы
     * @return ApiResponse с булевым флагом была-ли согласована работа
     */
    fun approveWorks(workIdListJSON: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_works", workIdListJSON)
            map
        }.flatMap { requestThingWorx("works_to_approve", it) }
    }

    /**
     * Метод для добавления комментариев к работе
     *
     * @param workId - идентификатор работы
     * @param text - текст
     * @return ApiResponse с булевым флагом был-ли добавлен комментарий
     */
    fun editWorkComment(workId: String, text: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map.put("work_comment", text)
            map
        }.flatMap { requestThingWorx("edit_work_comment", it) }
    }

    /**
     * Метод для добавления комментариев к замечанию
     *
     * @param remarkId - идентификатор работы
     * @param text - текст
     * @return ApiResponse с булевым флагом был-ли добавлен комментарий
     */
    fun editRemarkComment(remarkId: String, text: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map.put("remark_comment", text)
            map
        }.flatMap { requestThingWorx("edit_remark_comment", it) }
    }

    /**
     * Метод для получния списка возможных исполнителей для данной работы
     *
     * @param workId - идентификатор работы
     * @return ApiResponse со списком возможных исполнителей
     */
    fun getPerformersList(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("workers_for_work", it) }
    }

    /**
     * Метод для получния списка возможных исполнителей для данной работы
     *
     * @param workId - идентификатор работы
     * @param performersIdList - список ID исполнителей
     * @return ApiResponse с флагом были-ли добавлены исполнители
     */
    fun setPerformers(workId: String, performersIdList: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map.put("id_workers", performersIdList)
            map
        }.flatMap { requestThingWorx("change_workers", it) }
    }


    /**
     * Метод для получения списка возможных замечаний
     *
     * @return ApiResponse со списком всех возможных замечаний
     */
    fun getRemarksFromCatalog(): Single<ApiResponse> {
        return requestThingWorx("remarks", emptyMap())
    }

    /**
     * Метод для получения списка возможных работ
     *
     * @return ApiResponse со списком всех возможных работ
     */
    fun getWorksFromCatalog(remarkId: String, queryStr: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map.put("name_filter", queryStr)
            map
        }.flatMap { requestThingWorx("works_catalog", it) }
    }

    /**
     * Метод для получения списка работ по замеачанию
     *
     * @return ApiResponse со списком всех возможных работ
     */
    fun getWorksFromRemark(remarkId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map
        }.flatMap { requestThingWorx("works", it) }
    }

    /**
     * Метод для добавления списка работ
     *
     * @param remarkId - id замечания
     * @param workIdList - списко идентификаторов работ
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun addWorks(remarkId: String, workIdListJSON: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map.put("id_works", workIdListJSON)
            map
        }.flatMap { requestThingWorx("add_work", it) }
    }

    /**
     * Метод для добавления списка работ
     *
     * @param remarkId - id замечания
     * @param workId - идентификатор работы
     * @param repeats - количество повторений
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun addWork(remarkId: String, workId: String, repeats: Int): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("id_remark", remarkId)
            map.put("id_work", workId)
            map.put("repeats", repeats)
            map
        }.flatMap { requestThingWorx("add_work", it) }
    }

    /**
     * Метод для добавления замечания
     *
     * @param remarkId - id замечания
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun addRemark(remarkId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map
        }.flatMap { requestThingWorx("add_remark", it) }
    }

    /**
     * Метод для создания кастомного замечания
     *
     * @param remarkId - id замечания
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun createRemark(remarkName: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("name", remarkName)
            map
        }.flatMap { requestThingWorx("create_add_remark", it) }
    }

    /**
     * Метод для выбора замечания
     *
     * @param remarkId - id замечания
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun chooseRemark(remarkId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map
        }.flatMap { requestThingWorx("choise_remark", it) }
    }

    /**
     * Метод для удаления замечания
     *
     * @param remarkId - id замечания
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun deleteRemark(remarkId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map
        }.flatMap { requestThingWorx("revoke_remark_work", it) }
    }

    /**
     * Метод для удаления работы
     *
     * @param workId - id работы
     * @return ApiResponse с булевым флагом была-ли запущена работа
     */
    fun deleteWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("del_work", it) }
    }

    /**
     * Получает список работ по категории срочно
     *
     * @return ApiResponse со списком работ
     */
    fun getUrgent(): Single<ApiResponse> {
        return requestThingWorx("works_urgent", emptyMap())
    }

    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     *
     * @param workId - Id работы
     *  @return ApiResponse со список ТМЦ входящих в данную работы
     */
    fun getTMCInWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("tmc_by_work", it) }
    }

    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     *
     * @return ApiResponse со список ТМЦ
     */
    fun getTMC(queryStr: String, stockAvailable: Boolean): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("name_filter", queryStr)
            map.put("stock_available", stockAvailable)
            map
        }.flatMap { requestThingWorx("tmc", it) }
    }

    /**
     * Удалить ТМЦ из работы
     *
     * @param idWork - Id работы
     * @param idTMC - Id ТМЦ
     *
     *  @return ApiResponse с флагом успешности выполнения запроса
     */
    fun deleteTMC(idWork: String, idTMC: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", idWork)
            map.put("tmc_id", idTMC)
            map
        }.flatMap { requestThingWorx("delete_tmc", it) }
    }

    /**
     * Редактировать количество ТМЦ в работе
     *
     * @param idWork - Id работы
     * @param idTMC - ID ТМЦ
     * @param tmcAmount - Новое количество ТМЦ
     *
     * @return ApiResponse с флагом успешности выполнения запроса
     */
    fun editTMCInWork(idWork: String, idTMC: String, tmcAmount: Double): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("id_work", idWork)
            map.put("tmc_id", idTMC)
            map.put("tmc_amount", tmcAmount)
            map
        }.flatMap { requestThingWorx("edit_tmc_in_work", it) }
    }

    /**
     * Редактировать количество ТМЦ для списания
     *
     * @param idWork - Id работы
     * @param idTMC - ID ТМЦ
     * @param tmcAssigned- Новое количество ТМЦ
     *
     * @return ApiResponse с флагом успешности выполнения запроса
     */
    fun editTMCForWriteOff(idWork: String, idTMC: String, tmcAmount: Double): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("id_work", idWork)
            map.put("tmc_id", idTMC)
            map.put("tmc_assigned", tmcAmount)
            map
        }.flatMap { requestThingWorx("edit_tmc_for_write_off", it) }
    }


    /**
     * Получает список работ с ТМЦ для списания
     *
     * @param workIdListJSON - список Id работ, обернутый в JSON
     * @return ApiResponse c работами
     */
    fun getTmcForWriteOff(workIdListJSON: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("id_works", workIdListJSON)
            map
        }.flatMap { requestThingWorx("tmc_for_write_off", it) }
    }

    /**
     * Метод осуществляет частичную приемку работы
     *
     * @param workId - идентификатор работы
     * @param dividedTime - выделяемое время
     * @return резултат выполнения метода
     */
    fun partlyAcceptWork(workId: String, dividedTime: Long): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("id_work", workId)
            map.put("new_work_time", dividedTime)
            map
        }.flatMap { requestThingWorx("work_part_accept", it) }
    }

    /**
     * Метод разделяет работу на наряды
     *
     * @param workId - идентификатор работы
     * @param dividedTime - выделяемое время
     * @return резултат выполнения метода
     */
    fun divideWork(workId: String, dividedTime: Long): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("id_work", workId)
            map.put("new_work_time", dividedTime)
            map
        }.flatMap { requestThingWorx("work_divide", it) }
    }

    /**
     * Метод возвращает список работ без исполнителей и список бригад
     *
     * @return ApiResponse со списком работ и бригад
     */
    fun getWorkAndBrigadesForAssignment(): Single<ApiResponse> {
        return requestThingWorx("works_and_brigades_for_assignment", emptyMap())
    }

    /**
     * Метод назначает группу исполнителей для группы работ
     *
     * @return результат выполнения метода
     */
    fun assignGroupWorkersToWork(workList: String, workerList: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("work_list", workList)
            map.put("worker_list", workerList)
            map
        }.flatMap { requestThingWorx("assign_group_workers_to_works", it) }
    }

    /**
     * Метод для получения информации по замерам
     *
     * @param workId - идентификатор работы
     * @return ApiResponse со списком всех замеров
     */
    fun getWorkMeasurements(workId: String): Single<ApiResponse> {
        return requestThingWorx("work_measurements", mapOf("work_id" to workId))
    }

    /**
     * Метод для получения информации по замерам
     *
     * @param workId - идентификатор работы
     * @return ApiResponse со списком всех замеров
     */
    fun getWorkMeasurementsWithTaskStatus(workId: String): Single<ApiResponse> {
        return requestThingWorx("work_measurements_with_task_status", mapOf("work_id" to workId))
    }

    /**
     * Метод для запроса задания на проведение аппаратных замеров
     *
     * @param workId - идентификатор работы
     * @param sectionNumber - номер позиции секции
     * @param stage - этап замера
     * @return ApiResponse со списком всех замеров
     */
    fun getMeasurementsTask(workId: String, sectionNumber: String, stage: Int): Single<ApiResponse> {
        return requestThingWorx("task_for_hw_measurements", mapOf("work_id" to workId, "section_position_number" to sectionNumber, "measurement_stage" to stage))
    }

    /**
     * Метод для проверки готовности аппаратных замеров
     *
     * @param workId - идентификатор работы
     * @param taskId - идентификатор задания на получение аппаратных замеров
     * @return ApiResponse со списком
     */
    fun checkMeasurementsReady(workId: String, taskId: String, stage: Int): Single<ApiResponse> {
        return requestThingWorx("check_hw_measurements_ready", mapOf("work_id" to workId, "task_id" to taskId, "measurement_stage" to stage))
    }

    /**
     * Метод для отправки информации по замерам
     *
     * @return ApiResponse с результатом выполнения метода
     */
    fun editMeasure(workId: String, measure: String) = requestThingWorx("write_work_measurements", mapOf("work_id" to workId, "measurements_list" to measure))

    /**
     * Метод для первода работы в статус "Обходное решение" (WORKAROUND)
     *
     * @param workId - идентификатор работы
     * @return ApiResponse
     */
    fun workaround(workId: String) = requestThingWorx("workaround", mapOf("work_id" to workId))

    /**
     * Метод для запроса на получение ремкомплекта
     *
     * @param sectionId - идентификатор секции
     * @return ApiResponse
     */
    fun orderToRepair(orderToRepairRequest: OrderToRepairRequest) = requestThingWorx("set_order_to_repair", orderToRepairRequest)

    /**
     * Метод для полчения детальных данных о явке сотрудников
     *
     * @return Список бригад с информацией по явке сотрудников
     */
    fun brigadePresence() = requestThingWorx("workers_brigade", emptyMap())

    /**
     * Метод для добавления изменения явки по сотруднику
     *
     * @param
     * @return ApiResponse
     */
    fun changeWorkerPresence(workerPresenceRequest: WorkerPresenceRequest) = requestThingWorx("change_presence", workerPresenceRequest)

    /**
     * Метод для получения список шаблонов для данного мастера на текущей секции
     *
     * @return ApiResponse
     */
    fun assignmentTemplates() = requestThingWorx("templates", emptyMap())

    /**
     * Метод для выбора шаблона
     *
     * @return ApiResponse
     */
    fun setAssignmentTemplate(id: String, date: Long, workNight: Boolean) = requestThingWorx("set_template",
            mapOf("id_template" to id, "date" to date, "work_night" to workNight))

    /**
     * Метод для общения с сервером
     *
     * @return ApiResponse c данными
     */
    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }

    /**
     * Метод для общения с сервером
     *
     * @return ApiResponse c данными
     */
    private fun requestThingWorx(serviceName: String, request: Any): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, request)
    }
}