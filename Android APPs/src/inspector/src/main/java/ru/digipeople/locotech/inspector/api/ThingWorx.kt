package ru.digipeople.locotech.inspector.api

import android.content.Context
import io.reactivex.Single
import ru.digipeople.locotech.inspector.R
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.helper.AuthorizationMistakeException
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
     * Метод для авторизации
     *
     */
    fun auth(username: String, password: String): Single<ApiResponse> {
        return baseThingWorx.connectRx(username, password)
                .doOnSuccess { connected ->
                    if (!connected) {
                        throw AuthorizationMistakeException(context.getString(R.string.connection_error))
                    }
                }
                .flatMap {
                    requestThingWorx("signin", emptyMap())
                }
    }

    /**
     * Получает список всего оборудования, находящегося на ремонте
     *
     * @return ApiResponse со спсиском оборудования
     */
    fun getEquipment(): Single<ApiResponse> {
        return requestThingWorx("equipements", emptyMap())
    }


    /**
     * Метод устанавливает выбранное оборудование в качестве текущего
     *
     * @param sectionid - id выбранного оборудования
     * @return ApiResponse с флагом была-ли установлено оборудование в качестве текущей
     */
    fun setSelectedSection(sectionId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_section", sectionId)
            map
        }.flatMap { requestThingWorx("select_section", it) }
    }

    /**
     * Метод устанавливает выбранное оборудование в качестве текущего
     *
     * @param equipmentid - id выбранного оборудования
     * @return ApiResponse с флагом была-ли установлено оборудование в качестве текущей
     */
    fun setSelectedEquipment(equipmentid: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_equipment", equipmentid)
            map
        }.flatMap { requestThingWorx("select_equipement", it) }
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
    fun addRemark(remarkId: String) = requestThingWorx("add_remark", mapOf("id_remark" to remarkId))

    /**
     * Метод для получения списка возможных замечаний
     *
     * @return ApiResponse со списком всех возможных замечаний
     */
    fun getRemarksFromCatalog(): Single<ApiResponse> {
        return requestThingWorx("remarks", emptyMap())
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
     * Метод для удаления замечания
     *
     * @param remarkId - идентификатор замечания
     * @param reasonId - идентификатор причины удаления
     * @return ApiResponse с булевым флагом было ли удалено замечание
     */
    fun revokeRemarkWork(remarkId: String, reasonId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map.put("id_reason", reasonId)
            map
        }.flatMap { requestThingWorx("revoke_remark_work", it) }
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
     * Метод для получния списка возможных подписантов
     *
     * @param groupId - идентификатор работы
     * @return ApiResponse со списком возможных исполнителей
     */
    fun getSigners(groupId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", groupId)
            map
        }.flatMap { requestThingWorx("workers_for_work", it) }
    }

    /**
     * Метод для получения цикловых работ
     *
     * @return Список цикловых работ
     */
    fun getCyclicWorks(): Single<ApiResponse> {
        return requestThingWorx("cyclic_works", emptyMap())
    }

    /**
     * Метод для получения документов
     *
     * @return Список документов
     */
    fun getDocuments(): Single<ApiResponse> {
        return requestThingWorx("documents", emptyMap())
    }

    /**
     * Метод для получения замечаний ОТК
     *
     * @return Список замечаний ОТК
     */
    fun getOTKRemarks(): Single<ApiResponse> {
        return requestThingWorx("remarks_sld", emptyMap())
    }

    /**
     * Метод для проставления признака принятия работы/замечания сотрудником СЛД группе работ
     *
     * @param workId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun acceptGroupSLD(workIds: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_works", workIds)
            map
        }.flatMap { requestThingWorx("sld_works_accept", it) }
    }

    /**
     * Метод для снятия признака принятия работы/замечания сотрудником СЛД
     *
     * @param workId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun declineSLD(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("sld_work_decline", it) }
    }

    /**
     * Метод для проставления признака принятия работы/замечания сотрудником РЖД
     *
     * @param workId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun acceptGroupRZD(workIds: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_works", workIds)
            map
        }.flatMap { requestThingWorx("rzd_works_accept", it) }
    }

    /**
     * Метод для снятия признака принятия работы/замечания сотрудником РЖД
     *
     * @param workId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun declineRZD(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("rzd_work_decline", it) }
    }

    /**
     * Метод для получения замечаний РЖД
     *
     * @return Список замечаний ОТК
     */
    fun getRzdRemarks(): Single<ApiResponse> {
        return requestThingWorx("remarks_rzd", emptyMap())
    }

    /**
     * Метод для получения причин удаления замечания
     *
     * @return Список причин удаления
     */
    fun getDeclineReasons(): Single<ApiResponse> {
        return requestThingWorx("remark_deleting_reasons", emptyMap())
    }

    /**
     * Метод для получения сводной инофрмации
     *
     * @return Список сводной инофрмации
     */
    fun getSummary(): Single<ApiResponse> {
        return requestThingWorx("summary", emptyMap())
    }

    /**
     * Список документов для печати
     */
    fun documentsForPrinting(): Single<ApiResponse> {
        return requestThingWorx("documents_for_printing", emptyMap())
    }

    /**
     * Список категорий подписантов для печати
     */
    fun documentsSigners(): Single<ApiResponse> {
        return requestThingWorx("documents_signers", emptyMap())
    }

    /**
     * Метод для формирования указанных документов для печати по указанному идентификатору секции с заданным списком подписантов
     */
    fun printProtocol(emailAcceptor: String, documentList: String, signersCategoryList: String): Single<ApiResponse> {
        return requestThingWorx("print_protocol", mapOf(
                "email_acceptor" to emailAcceptor,
                "documents_list" to documentList,
                "category_signer_list" to signersCategoryList
        ))
    }

    /**
     * Список должностных лиц
     *
     * @return Чек-лист приемки
     */
    fun getExecutiveList(executiveName: String): Single<ApiResponse> {
        return requestThingWorx("executive_list", mapOf("executive_name_substring" to executiveName))
    }

    /**
     * Метод для получения контрольных точек
     */
    fun getControlPoints(workId: String): Single<ApiResponse> {
        return requestThingWorx("work_checkpoints", mapOf("id_work" to workId))
    }

    /**
     * Метод для получения чеклиста
     */
    fun getChecklist() = requestThingWorx("checklist", emptyMap())

    /**
     * Метод для проставления признака принятия Контрольно-Сервисной операции(КСО) сотрудником СЛД
     *
     * @param csoId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun acceptCsoSLD(csoId: String) = requestThingWorx("sld_cso_accept", mapOf("id_cso" to csoId))

    /**
     * Метод для снятия признака принятия ксо сотрудником СЛД
     *
     * @param csoId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun declineCsoSLD(csoId: String) = requestThingWorx("sld_cso_decline", mapOf("id_cso" to csoId))


    /**
     * Метод для проставления признака принятия Контрольно-Сервисной операции(КСО) сотрудником РЖД
     *
     * @param csoId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun acceptCsoRzd(csoId: String) = requestThingWorx("rzd_cso_accept", mapOf("id_cso" to csoId))

    /**
     * Метод для снятия признака принятия ксо сотрудником РЖД
     *
     * @param csoId - id Принимаемой работы
     * @return  ApiResponse с флагом успешности перевода
     */
    fun declineCsoRzd(csoId: String) = requestThingWorx("rzd_cso_decline", mapOf("id_cso" to csoId))

    /**
     * Метод для добавления комментариев к ксо
     *
     * @param csoId - идентификатор работы
     * @param text - текст
     * @return ApiResponse с булевым флагом был-ли добавлен комментарий
     */
    fun editCsoComment(csoId: String, text: String) = requestThingWorx("edit_work_comment", mapOf("id_cso" to csoId, "cso_comment" to text))

    /**
     * Метод для вызова представителя РЖД
     *
     * @return ApiResponse с булевым флагом был-ли вызван представитель РЖД
     */
    fun notifyRzd() = requestThingWorx("notify_rzd", emptyMap())

    /**
     * Принять чек-лист
     *
     * @return ApiResponse с булевым флагом был-ли принят чек лист
     */
    fun acceptInspectionRzd() = requestThingWorx("accept_rzd_inspection", emptyMap())

    /**
     * Получить количество непринятых операций в чек листе
     *
     * @return ApiResponse с количеством операцмй
     */
    fun getNotAcceptedCount() = requestThingWorx("not_accepted_count", emptyMap())

    /**
     * Метод для получения информации по замерам
     *
     * @return ApiResponse со списком всех замеров
     */
    fun getWorkMeasurements(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, Any>()
            map.put("work_id", workId)
            map
        }.flatMap { requestThingWorx("work_measurements", it) }
    }

    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }
    /**
     * Метод для установки электронной почты пользователя
     */
    fun setEmailForPrint(email: String): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx("set_email_for_print", mapOf("email_for_print" to email))
    }
}