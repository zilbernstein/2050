package ru.digipeople.locotech.worker.api

import android.content.Context
import io.reactivex.Single
import ru.digipeople.locotech.worker.R
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.helper.AuthorizationMistakeException
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс [ThingWorx] - Api МП Сотрудник
 *
 * @author Kashonkov Nikita
 */
@Singleton
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx, private val context: Context) {
    /**
     * Метод для авторизации
     *
     * @param username Имя пользователя
     * @param password Пароль
     *
     * @return ApiReponse с информацией о пользователе
     */
    fun auth(username: String, password: String, fbcToken: String): Single<ApiResponse> {
        return baseThingWorx.connectRx(username, password)
                .doOnSuccess { connected ->
                    if (!connected) {
                        throw AuthorizationMistakeException(context.getString(R.string.connection_error))
                    }
                }
                .flatMap {
                    requestThingWorx("signin_worker", mapOf("token" to fbcToken))
                }
    }

    /**
     * Метод для запуска смены
     *
     * @return ApiReponse с флагом запущена ли смена
     */
    fun startShift(): Single<ApiResponse> {
        return requestThingWorx("shift_start", emptyMap())
    }

    /**
     * Метод для остановки смены
     *
     * @return ApiReponse с флагом завершена ли смена
     */
    fun stopShift(): Single<ApiResponse> {
        return requestThingWorx("shift_stop", emptyMap())
    }

    /**
     * Метод для получения списка работ
     *
     * @return ApiReponse с информацией об оборудовании и работах
     */
    fun getWorks(): Single<ApiResponse> {
        return requestThingWorx("my_works", emptyMap())
    }

    /**
     * Метод для получения информации по работе
     *
     * @param workId - Id работы
     * @return ApiResponse с информацией о работе
     */
    fun getWorkDetail(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("work_detail", it) }
    }

    /**
     * Метод для начала выполнения работы
     *
     * @param workId - Id работы
     * @return ApiResponse с флагом об успешности начала выполнения работы
     */
    fun startWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("work_to_execute", it) }
    }

    /**
     * Метод для постановки работы на паузу
     *
     * @param workId - Id работы
     * @param reasonId - Id причины
     * @return ApiResponse с флагом об успешности начала выполнения работы
     */
    fun pauseWork(workId: String, reasonId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map.put("id_reason", reasonId)
            map
        }.flatMap { requestThingWorx("work_pause", it) }
    }

    /**
     * Отметить работу как выполненную
     *
     * @param workId - Id работы
     * @return ApiResponse с флагом об успешности начала выполнения работы
     */
    fun doneWork(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("work_done", it) }
    }

    /**
     * Метод для получения списка работ
     *
     * @return ApiReponse с информацией о списке причин остановки
     */
    fun getReasons(): Single<ApiResponse> {
        return requestThingWorx("reasons", emptyMap())
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
     * Метод для общения с сервером
     *
     * @return ApiResponse c данными
     */
    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
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
     *  Метод отправки информации по замерам
     *
     *  @param workId - идентификатор экземпляра работы
     *  @param measurements - список объектов типа MeasurementChangeRequest в виде JSON
     */
    fun postChangeMeasurement(workId: String, measurements: String): Single<ApiResponse> {
        return requestThingWorx("write_work_measurements_v2", mapOf("work_id" to workId, "measurements" to measurements))
    }

    /**
     * Метод для получения чеклиста по работе
     *
     * @param workId - идентификатор экземпляра работы
     */
    fun getChecklist(workId: String): Single<ApiResponse> {
        return requestThingWorx("work_checklist", mapOf("work_id" to workId))
    }

    /**
     * Метод для проставления признака проверки строки чеклиста
     *
     * @param workId - идентификатор экземпляра работы
     * @param checklistItemId - Идентификатор строки чеклиста
     */
    fun postCheckChecklistItem(workId: String, checklistItemId: String): Single<ApiResponse> {
        return requestThingWorx("check_checklist_item", mapOf("work_id" to workId, "checklist_item_id" to checklistItemId))
    }

    /**
     * Метод для снятия признака проверки строки чеклиста
     *
     * @param workId - идентификатор экземпляра работы
     * @param checklistItemId - Идентификатор строки чеклиста
     */
    fun postUncheckChecklistItem(workId: String, checklistItemId: String): Single<ApiResponse> {
        return requestThingWorx("uncheck_checklist_item", mapOf("work_id" to workId, "checklist_item_id" to checklistItemId))
    }
}