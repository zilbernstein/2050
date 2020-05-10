package ru.digipeople.locotech.worker.api

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.model.WorkDetailEntity
import ru.digipeople.locotech.worker.api.model.response.*
import ru.digipeople.locotech.worker.model.MeasurementChangeRequest
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Интерфейс по работе с Апи МП Сотрудник
 *
 * @author Kashonkov Nikita
 */
interface ThingsWorxWorker {
    /**
     * Метод авторизации
     *
     * @param username - имя пользователя
     * @param password - пароль
     *
     * @return AuthEsponse с информацией о подключенном пользователе
     */
    fun auth(username: String, password: String, fbcToken: String): Single<AuthResponse>

    /**
     * Метод для запуска смены
     *
     * @return ApiReponse с данными
     */
    fun startShift(): Single<ResultResponse>

    /**
     * Метод для становки смены
     *
     * @return ApiReponse с данными
     */
    fun stopShift(): Single<ResultResponse>

    /**
     * Получить список работ пользоваетля
     *
     * @return список работ и оборудования
     */
    fun getWorks(): Single<MyWorksResponse>

    /**
     * Получить данные по работе
     *
     * @param workId - id работы
     *
     */
    fun getWorkDetail(workId: String): Single<WorkDetailEntity>

    /**
     * Запустить работу
     *
     * @param workId - id работы
     * @return результат выполнения запроса
     */
    fun startWork(workId: String): Single<ResultResponse>

    /**
     * Отметить работу как выполненую
     *
     * @param workId - id работы
     * @return результат выполнения запроса
     */
    fun doneWork(workId: String): Single<ResultResponse>

    /**
     * Поставить работу на паузу
     *
     * @param workId - id работы
     * @return результат выполнения запроса
     */
    fun pauseWork(workId: String, reasonId: String): Single<ResultResponse>

    /**
     * Получить список причин отказа
     *
     * @return список причин отказа
     */
    fun getReasons(): Single<PauseReasonResponse>

    /**
     * Метод для добавления комментария к работе
     *
     * @param workId - идентификатор работы
     * @param text - текст комментария
     * @return Флаг была-ли запущена работа
     */
    fun addWorkComment(workId: String, text: String): Single<ResultResponse>

    /**
     * Метод для получения списка ТМЦ по работе
     *
     * @param workId - идентификатор работы
     * @return список ТМЦ
     */
    fun loadTMCByWork(workId: String): Single<TMCInWorkResponse>

    /**
     * Метод для получения информации по замерам
     *
     * @param workId id работы
     */
    fun getWorkMeasurements(workId: String): Single<MeasurementsResponse>

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
     *  Метод отправки информации по замерам
     *
     *  @param workId - идентификатор экземпляра работы
     *  @param requests - список моделей запросов с измененными св-вами Measurement
     */
    fun postChangeMeasurement(workId: String, requests: List<MeasurementChangeRequest>): Single<ResultResponse>

    /**
     * Метод для получения чек-листа по работе
     *
     * @param workId - идентификатор работы
     */
    fun getChecklist(workId: String): Single<ChecklistResponse>

    /**
     * Метод для проставления признака проверки строки чеклиста
     *
     * @param workId - идентификатор экземпляра работы
     * @param checklistItemId - Идентификатор строки чеклиста
     */
    fun postCheckChecklistItem(workId: String, checklistItemId: String): Single<ResultResponse>

    /**
     * Метод для снятия признака проверки строки чеклиста
     *
     * @param workId - идентификатор экземпляра работы
     * @param checklistItemId - Идентификатор строки чеклиста
     */
    fun postUncheckChecklistItem(workId: String, checklistItemId: String): Single<ResultResponse>
}