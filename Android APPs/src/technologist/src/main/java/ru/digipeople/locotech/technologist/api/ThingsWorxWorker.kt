package ru.digipeople.locotech.technologist.api

import io.reactivex.Single
import ru.digipeople.locotech.technologist.api.response.AuthResponse
import ru.digipeople.locotech.technologist.api.response.EquipmentResponse
import ru.digipeople.locotech.technologist.api.response.WorkDetailResponse
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Интерфейс для работы с Апи МП Технолог
 *
 * @author Kashonkov Nikita
 */
interface ThingsWorxWorker {
    /**
     * Метод авторизации
     */
    fun auth(username: String, password: String, fbcToken: String): Single<AuthResponse>

    /**
     * Получает список всех замечаний и работ
     */
    fun getRemarkAndWork(): Single<EquipmentResponse>

    /**
     * Получает информацию о работе
     */
    fun getWorkDetail(workId: String): Single<WorkDetailResponse>

    /**
     * Получает информацию о работе
     * @return Флаг была-ли согласована работа
     */
    fun setWorkCheck(workList: List<String>): Single<ResultResponse>

    /**
     * Получает информацию о работе
     * @return Флаг была-ли согласована работа
     */
    fun setWorkUncheck(workList: List<String>): Single<ResultResponse>

    /**
     * Завершает согласование по замечанию
     *
     * @param remarkId - id замечания
     * @param workIds - список работ для завершения согласования
     * @return ApiResponse с флагом была ли согласована работа
     */
    fun approveWorks(remarkId: String, workIds: List<String>): Single<ResultResponse>

    /**
     * Отклоняет согласование по замечанию
     *
     * @param remarkId - id замечания
     * @param workIds - список работ для отклонения согласования
     * @return ApiResponse с флагом была ли согласована работа
     */
    fun rejectWorks(remarkId: String, workIds: List<String>): Single<ResultResponse>

    /**
     * Меняет комментарий к выбранной работе
     *
     * @param idWork - id работы
     * @param text - текст комментария
     * @return ApiResponse с флагом был ли изменён комментарий
     */
    fun setWorkComment(idWork: String, text: String): Single<ResultResponse>
}