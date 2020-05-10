package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName

/**
 * Работа для назначения работников
 *
 * @author Sostavkin Grisha
 */
class WorkForWorkerAssignmentEntity {
    /**
     * id работы
     */
    @SerializedName("id_work")
    lateinit var id: String
    /**
     * Наименование работы
     */
    @SerializedName("work_name")
    lateinit var workName: String
    /**
     * Нормативное время
     */
    @SerializedName("time_limit")
    var timeLimit = 0L
    /**
     * Оставшееся время
     */
    @SerializedName("time_remain")
    var timeRemainL = 0L
    /**
     * Процент остатка наряда
     */
    @SerializedName("work_part_percent")
    var workPercent: Int = 0
    /**
     * Время остатка наряда
     */
    @SerializedName("work_part_time")
    var workPartTime: Int = 0
    /**
     * Максимальное число исполнителей
     */
    @SerializedName("worker_limit")
    var workerLimit: Int = 0
    /**
     * Номер заказа-наряда
     */
    @SerializedName("order_number")
    lateinit var orderNumber: String
    /**
     * Количество повторов
     */
    @SerializedName("repeats")
    var repeat: Int = 0
}