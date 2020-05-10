package ru.digipeople.locotech.inspector.api

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.model.response.*
import ru.digipeople.thingworx.model.SignersCategoryEntity
import ru.digipeople.thingworx.model.response.ResultResponse

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
     * Получает список всего оборудования, находящегося на ремонте
     */
    fun getEquipment(): Single<EquipmentResponse>

    /**
     * Метод устанавливает выбранно1 секции в качестве текущего
     *
     * @param equipmentId - id выбранного оборудования
     * @return Флаг была-ли установлено оборудование в качестве текущей
     */
    fun setSelectedSection(equipmentId: String): Single<ResultResponse>

    /**
     * Метод устанавливает выбранного линейного оборудования в качестве текущего
     *
     * @param equipmentId - id выбранного оборудования
     * @return Флаг была-ли установлено оборудование в качестве текущей
     */
    fun setSelectedEquipment(equipmentId: String): Single<ResultResponse>

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
    fun addRemark(remarkId: String): Single<CreateRemarkResponse>

    /**
     * Метод для получения списка возможных замечаний
     *
     * @return Список замечаний из катаалога
     */
    fun getRemarksFromCatalog(): Single<RemarkFromCatalogResponse>

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
     * Метод для получния списка возможных подписантов
     *
     * @param groupId - идентификатор группы подписи
     * @return Cписок возможных исполнителей
     */
    fun getSignerList(groupId: String): Single<SignerResponse>

    /**
     * Метод для получения цикловых работ
     *
     * @return Список цикловых работ
     */
    fun getCyclicWorks(): Single<CyclicWorkResponse>

    /**
     * Метод для получения документов
     *
     * @return Список документов
     */

    fun getDocuments(): Single<DocumentResponse>

    /**
     * Метод для получения замечаний ОТК
     *
     * @return Список замечаний ОТК
     */
    fun getOTKRemarks(): Single<RemarkSldResponse>

    /**
     * Метод для получения замечаний РЖД
     *
     * @return Список замечаний ОТК
     */
    fun getRzdRemarks(): Single<RemarkRzdResponse>

    /**
     * Метод для проставления признака принятия работы/замечания сотрудником СЛД группе работ
     *
     * @param workId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun acceptSLD(workIds: List<String>): Single<ResultResponse>

    /**
     * Метод для снятия признака принятия работы/замечания сотрудником СЛД
     *
     * @param workId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun declineSLD(workId: String): Single<ResultResponse>

    /**
     * Метод для проставления признака принятия работы/замечания сотрудником РЖД группе работ
     *
     * @param workId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun acceptRZD(workIds: List<String>): Single<ResultResponse>

    /**
     * Метод для снятия признака принятия работы/замечания сотрудником РЖД
     *
     * @param workId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun declineRZD(workId: String): Single<ResultResponse>

    /**
     * Метод для получения причин удаления замечаний
     *
     * @return Список причин удаления
     */
    fun getDeclinedReasons(): Single<DeclineReasonResponse>

    /**
     * Список должностных лиц
     */
    fun getExecutiveList(executiveNameSubstring: String): Single<SignerListResponse>

    /**
     * Метод для получения сводной информации
     *
     * @return Список сводной информации
     */
    fun getSummary(): Single<SummaryResponse>

    /**
     * Список документов для печати
     */
    fun documentsForPrinting(): Single<DocumentsForPrintingResponse>

    /**
     * Список категорий подписантов для печати
     */
    fun documentsSigners(): Single<DocumentsSignersResponse>

    /**
     * Инициация печати протокола осмотра
     */
    fun printProtocol(emailAcceptor: String, documentList: List<String>, signersCategoryList: List<SignersCategoryEntity>): Single<ResultResponse>

    /**
     * Обновить почту для печати
     */
    fun setEmailForPrint(email: String): Single<ResultResponse>

    /**
     * Список контрольных точек
     *
     * @return Список контрольных точек
     */
    fun getControlPoint(workId: String): Single<ControlPointsResponse>

    /**
     * Метод для получения чеклиста
     *
     * @return Чек лист
     */
    fun getChecklist(): Single<ChecklistResponse>

    /**
     * Метод для проставления признака принятия Контрольно-Сервисной операции(КСО) сотрудником СЛД
     *
     * @param csoId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun acceptCsoSLD(csoId: String): Single<ResultResponse>

    /**
     * Метод для снятия признака принятия ксо сотрудником СЛД
     *
     * @param csoId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun declineCsoSLD(csoId: String): Single<ResultResponse>

    /**
     * Метод для проставления признака принятия Контрольно-Сервисной операции(КСО) сотрудником РЖД
     *
     * @param csoId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun acceptCsoRZD(csoId: String): Single<ResultResponse>

    /**
     * Метод для снятия признака принятия ксо сотрудником РЖД
     *
     * @param csoId - id Принимаемой работы
     * @return флаг успешности перевода
     */
    fun declineCsoRZD(csoId: String): Single<ResultResponse>

    /**
     * Метод для удаления замечания
     *
     * @param remarkId - id замечания
     * @param reasonId - id причины удаления
     * @return флаг успешности перевода
     */
    fun revokeRemark(remarkId: String, reasonId: String): Single<ResultResponse>

    /**
     * Метод для добавления комментариев к ксо
     *
     * @param csoId - идентификатор работы
     * @param text - текст
     * @return Флаг был- ли изменен комментарий
     */
    fun editCsoComment(csoId: String, text: String): Single<ResultResponse>

    /**
     * Метод для вызова представителя РЖД
     *
     * @return флаг был-ли вызван представитель РЖД
     */
    fun notifyRzd(): Single<ResultResponse>

    /**
     * Принять чек-лист
     *
     * @return флаг был-ли принят чек лист
     */
    fun acceptInspectionRzd(): Single<ResultResponse>

    /**
     * Получить количество непринятых операций в чек листе
     *
     * @return Количество непринятых операцмй
     */
    fun getNotAcceptedCount(): Single<ResultResponse>

    /**
     * Метод для получения информации по замерам
     *
     * @param workId id работы
     */
    fun getWorkMeasurements(workId: String): Single<MeasurementsResponse>
}