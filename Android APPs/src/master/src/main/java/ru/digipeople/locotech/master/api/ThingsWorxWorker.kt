package ru.digipeople.locotech.master.api

import io.reactivex.Single
import ru.digipeople.locotech.master.api.model.WorksAndBrigadesEntity
import ru.digipeople.locotech.master.api.model.request.OrderToRepairRequest
import ru.digipeople.locotech.master.api.model.request.WorkerPresenceRequest
import ru.digipeople.locotech.master.api.model.response.*
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Интерфейс для [ThingsWorxWorker]
 *
 * @author Kashonkov Nikita
 */
interface ThingsWorxWorker {
    /**
     * Получает список всего оборудования, находящегося на ремонте
     */
    fun getEquipment(): Single<EquipmentResponse>

    /**
     * Получает список всех замечаний по оборудованию
     */
    fun getRemarkByEquipment(): Single<EquipmentRemarkResponse>

    /**
     * Получает список работ по оборудованию
     *
     * @return Cписок работ
     */
    fun getWorksByEquipment(): Single<WorkResponse>

    /**
     * Получает список работ для согласования и запуска
     *
     * @return Cписок работ
     */
    fun getWorksForStartAndApprove(): Single<WorkResponse>

    /**
     * Метод для получения списка работ, которые можно отправить на согласования
     *
     * @return InfoTable со списком работ
     */
    fun getWorksReadyForApprove(): Single<WorkResponse>

    /**
     * Метод для получения списка работ, которые можно запустить
     *
     * @return InfoTable со списком работ
     */
    fun getWorksReadyForStart(): Single<WorkResponse>

    /**
     * Метод для получения списка работ, которые можно принять
     *
     * @return InfoTable со списком работ
     */
    fun getWorksReadyForMasterAccept(): Single<WorkResponse>

    /**
     * Метод для приостановки работы
     *
     * @param workId - идентификатор работы
     * @return Флаг была-ли приостановлена работа
     */
    fun stopWork(workId: String): Single<ResultResponse>

    /**
     * Метод для принятия работы
     *
     * @param workId - идентификатор работы
     * @return Флаг была-ли принята работа
     */
    fun acceptWork(workId: String): Single<ResultResponse>

    /**
     * Метод для приемки всех работ
     *
     * @param workIdList - список работ для запуска
     * @return Флаг были-ли приняты работы
     */
    fun acceptWorks(workIdList: List<String>): Single<ResultResponse>

    /**
     * Метод для запуска работы
     *
     * @param workId - идентификатор работы
     * @return Флаг быыла-ли запущена работа
     */
    fun startWork(workId: String): Single<ResultResponse>

    /**
     * Метод для запуска списка работ
     *
     * @param workIdList - список работ для запуска
     * @return Флаг были-ли запущены работы
     */
    fun startWork(workIdList: List<String>): Single<ResultResponse>

    /**
     * Метод для согласования работ
     *
     * @param workIdList - идентификатор работы
     * @return Флаг были-ли работы отправлены на согласование
     */
    fun approveWork(workIdList: List<String>): Single<ResultResponse>

    /**
     * Метод для добавления комментария к работе
     *
     * @param workId - идентификатор работы
     * @param text - текст комментария
     * @return Флаг была-ли запущена работа
     */
    fun addWorkComent(workId: String, text: String): Single<ResultResponse>

    /**
     * Метод для добавления комментария к замечанию
     *
     * @param workId - идентификатор работы
     * @param text - текст комментария
     * @return Флаг была-ли запущена работа
     */
    fun addRemarkComent(remarkId: String, text: String): Single<ResultResponse>

    /**
     * Метод для получния списка возможных исполнителей для данной работы
     *
     * @param workId - идентификатор работы
     * @return Cписок возможных исполнителей
     */
    fun getPerformersList(workId: String): Single<PerformerResponse>

    /**
     * Метод для получния списка возможных исполнителей для данной работы
     *
     * @param workId - идентификатор работы
     * @param performersIdList - список ID исполнителей
     * @return Флаг были-ли назначены работники
     */
    fun setPerformers(workId: String, performersIdList: List<String>): Single<ResultResponse>

    /**
     * Метод для получения списка возможных замечаний
     *
     * @return Список замечаний из катаалога
     */
    fun getRemarksFromCatalog(): Single<RemarkFromCatalogResponse>

    /**
     * Метод для получения списка возможных работ
     *
     * @return Список работ из каталога
     */
    fun getWorksFromCatalog(remarkId: String, queryStr: String): Single<WorkFromCatalogResponse>

    /**
     * Метод для получения списка работ из замечания
     *
     * @return Список работ из каталога
     */
    fun getWorksFromRemark(remarkId: String): Single<WorkResponse>

    /**
     * Метод для добавление списка работ в замечание
     *
     * @param remarkId - id замечания
     * @param workIdList - идентификатор работы
     * @return Флаг были-ли добавлены работы
     */
    fun addWorks(remarkId: String, workIds: Collection<String>): Single<ResultResponse>

    /**
     * Метод для добавление списка работ в замечание
     *
     * @param remarkId - id замечания
     * @param workId - идентификатор работы
     * @param repeatsCount - количество повторов
     * @return Флаг были-ли добавлены работы
     */
    fun addWorks(remarkId: String, workId: String, repeatsCount: Int): Single<ResultResponse>

    /**
     * Метод для создания кастомного замечания
     *
     * @param remarkId - id замечания
     * @return Флаг было-ли создано замечание
     */
    fun createRemark(remarkName: String): Single<CreateRemarkResponse>

    /**
     * Метод для выбора замечания
     *
     * @param remarkId - id замечания
     * @return Флаг было-ли добавлено замечание
     */
    fun chooseRemark(choiceId: String): Single<CreateRemarkResponse>

    /**
     * Метод для добавления замечания
     *
     * @param remarkId - id замечания
     * @return Флаг было-ли добавлено замечание
     */
    fun addRemark(remarkId: String): Single<ResultResponse>

    /**
     * Метод для удаления замечания
     *
     * @param remarkId - id замечания
     * @return Флаг было-ли добавлено замечание
     */
    fun deleteRemark(remarkId: String): Single<ResultResponse>

    /**
     * Метод для удаления работы
     *
     * @param remarkId - id замечания
     * @return Флаг было-ли добавлено замечание
     */
    fun deleteWork(workId: String): Single<ResultResponse>

    /**
     * Метод для получения списка Срочно
     *
     * @return Список работ
     */
    fun getUrgent(): Single<WorkResponse>

    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     *
     * @param workId - Id работы
     * @return список ТМЦ
     */
    fun getTMCInWork(workId: String): Single<TMCInWorkResponse>

    /**
     * Метод возвращает ТМЦ прикрепленные к конкретной работе
     *
     * @return список ТМЦ
     */
    fun getTMC(queryStr: String, stockAvailable: Boolean): Single<TMCResponse>

    /**
     * Удалить ТМЦ из работы
     *
     * @param idWork - Id работы
     * @param idTMC - Id ТМЦ
     *
     * @return Флаг успешности выполнения запроса
     */
    fun deleteTMC(idWork: String, idTMC: String): Single<ResultResponse>

    /**
     * Редактировать количество ТМЦ в работе
     *
     * @param idWork - Id работы
     * @param idTMC - ID ТМЦ
     * @param tmcAmount - Новое количество ТМЦ
     *
     * @return Флаг успешности выполнения запроса
     */
    fun editTMCInWork(idWork: String, idTMC: String, tmcAmount: Double): Single<ResultResponse>

    /**
     * Редактировать количество ТМЦ для списания
     *
     * @param idWork - Id работы
     * @param idTMC - ID ТМЦ
     * @param tmcAmount - Новое количество ТМЦ
     *
     * @return Флаг успешности выполнения запроса
     */
    fun editTMCForWriteOff(idWork: String, idTMC: String, tmcAmount: Double): Single<ResultResponse>


    /**
     * Получает список работ с ТМЦ для списания
     *
     * @param workIdListJSON - список Id работ, обернутый в JSON
     * @return список тмц по работам
     */
    fun getTmcForWriteOff(workIds: Collection<String>): Single<WriteOffTmcResponse>

    /**
     * Метод разделяет работу на наряды
     *
     * @param workId - идентификатор работы
     * @param dividedTime - выделяемое время
     * @return резултат выполнения метода
     */
    fun divideWork(workId: String, dividedTime: Long): Single<ResultResponse>

    /**
     * Метод осуществляет частичную приемку работы
     *
     * @param workId - идентификатор работы
     * @param dividedTime - выделяемое время
     * @return резултат выполнения метода
     */
    fun partlyAcceptWork(workId: String, dividedTime: Long): Single<ResultResponse>

    /**
     * Получает список работ без исполнителей и список бригад
     *
     * @return Возвращает список работ без исполнителей и список бригад
     */
    fun getWorkAndBrigadesForAssignment(): Single<WorksAndBrigadesEntity>

    /**
     * Метод назначает группу исполнителей для группы работ
     *
     * @param workList список работ
     */
    fun assignGroupWorkersToWorks(workList: Collection<String>, workerList: Collection<String>): Single<ResultResponse>

    /**
     * Метод для получения информации по замерам
     *
     * @param workId id работы
     */
    fun getWorkMeasurements(workId: String): Single<MeasurementsResponse>

    /**
     * Метод для получения информации по замерам
     *
     * @param workId id работы
     */
    fun getWorkMeasurementsWithTaskStatus(workId: String): Single<MeasurementsWithStatusResponse>

    /**
     * Метод для запроса задания на проведение аппаратных замеров
     *
     * @param workId - идентификатор работы
     * @param sectionNumber - номер позиции секции
     * @param stage - этап замера
     */
    fun getMeasurementsTask(workId: String, sectionNumber: String, stage: Int): Single<MeasurementsTaskResponse>

    /**
     * Метод для проверки готовности аппаратных замеров
     *
     * @param workId - идентификатор работы
     * @param taskId - идентификатор задания на получение аппаратных замеров
     */
    fun checkMeasurementsReady(workId: String, taskId: String, stage: Int): Single<MeasurementsWithStatusResponse>

    /**
     * Метод для отправки информации по замерам
     *
     * @param workId id работы
     * @param measureList список замеров
     */
    fun editMeasure(workId: String, measure: IndicatorResponse): Single<ResultResponse>

    /**
     * Метод для первода работы в статус "Обходное решение" (WORKAROUND)
     *
     * @param workId - идентификатор работы
     * @return ApiResponse
     */
    fun workaround(workId: String): Single<ResultResponse>

    /**
     * Метод для полчения детальных данных о явке сотрудников
     *
     * @return Список бригад с информацией по явке сотрудников
     */
    fun brigadePresence(): Single<BrigadePresenceResponse>

    /**
     * Метод для добавления изменения явки по сотруднику
     *
     * @param
     * @return ApiResponse
     */
    fun changeWorkerPresence(presenceRequest: WorkerPresenceRequest): Single<ChangePresenceResponse>

    /**
     * Метод для получения список шаблонов для данного мастера на текущей секции
     *
     * @return ApiResponse
     */
    fun assignmentTemplates(): Single<AssignmentTemplatesResponse>

    /**
     * Метод для выбора шаблона
     *
     * @return ApiResponse
     */
    fun setAssignmentTemplate(id: String, date: Long, workNight: Boolean): Single<ResultResponse>

    /**
     * Метод запроса на получение ремкомплекта
     */
    fun orderToRepair(orderToRepairRequest: OrderToRepairRequest): Single<OrderToRepairResponse>
}