package ru.digipeople.locotech.technologist.api

import android.content.Context
import io.reactivex.Single
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.api.request.ApproveWorksRequest
import ru.digipeople.locotech.technologist.api.request.RejectWorksRequest
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.helper.AuthorizationMistakeException
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс [ThingWorx] - Api МП Технолог
 *
 * @author Kashonkov Nikita
 */
@Singleton
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx, private val context: Context) {

    /**
     * Метод для авторизации
     *
     */
    fun auth(username: String, password: String, fbcToken: String): Single<ApiResponse> {
        return baseThingWorx.connectRx(username, password)
                .doOnSuccess { connected ->
                    if (!connected) {
                        throw AuthorizationMistakeException(context.getString(R.string.connection_error))
                    }
                }
                .flatMap {
                    requestThingWorx("signin", mapOf("token" to fbcToken))
                }
    }

    /**
     * Получает список всех замечаний и работ
     *
     * @return ApiResponse со списоком замечаний и работ
     */
    fun getRemarkAndWork(): Single<ApiResponse> {
        return requestThingWorx("approval_remark_work", emptyMap())
    }

    /**
     * Получает список деталки работы
     *
     * @return ApiResponse со списоком деталки работы
     */
    fun getWorkDetail(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { hashMap ->
            requestThingWorx("work_detail", hashMap)
        }
    }

    /**
     * Выбирает работу для согласования
     *
     * @param workList - список согласованных работ
     * @return ApiResponse с флагом была ли согласована работа
     */
    fun setWorkCheck(workList: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("works_list", workList)
            map
        }.flatMap { hashMap ->
            requestThingWorx("works_check", hashMap)
        }
    }

    /**
     * Выбирает работу для снятия согласования
     *
     * @param workList - список несогласованных работ
     * @return ApiResponse с флагом была ли согласована работа
     */
    fun setWorkUncheck(workList: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("works_list", workList)
            map
        }.flatMap { hashMap ->
            requestThingWorx("works_uncheck", hashMap)
        }
    }

    /**
     * Завершает согласование по замечанию
     *
     * @param remarkId - id замечания
     * @param workIds - список работ для завершения согласования
     * @return ApiResponse с флагом была ли согласована работа
     */
    fun approveWorks(remarkId: String, workIds: List<String>): Single<ApiResponse> {
        return requestThingWorx("works_approve", ApproveWorksRequest().apply {
            this.remarkId = remarkId
            this.workIds = workIds
        })
    }

    /**
     * Отклоняет согласование по замечанию
     *
     * @param remarkId - id замечания
     * @param workIds - список работ для отклонения согласования
     * @return ApiResponse с флагом была ли согласована работа
     */
    fun rejectWorks(remarkId: String, workIds: List<String>): Single<ApiResponse> {
        return requestThingWorx("works_decline", RejectWorksRequest().apply {
            this.remarkId = remarkId
            this.workIds = workIds
        })
    }

    /**
     * Меняет комментарий к выбранной работе
     *
     * @param idWork - id работы
     * @param text - текст комментария
     * @return ApiResponse с флагом был ли изменён комментарий
     */
    fun setWorkComment(idWork: String, text: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", idWork)
            map.put("work_comment", text)
            map
        }.flatMap { hashMap ->
            requestThingWorx("edit_work_comment", hashMap)
        }
    }

    /**
     * Метод для общения с сервером
     *
     * @return ApiResponse c данными
     */
    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }

    private fun requestThingWorx(serviceName: String, request: Any): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, request)
    }
}