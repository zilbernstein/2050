package ru.digipeople.locotech.master.api.model.request

import com.google.gson.annotations.SerializedName
/**
 * Модель параметров запроса установки явки сотрудника
 */
class WorkerPresenceRequest(
        /*
        * UUID сотрудника
        */
        @SerializedName("worker_id")
        val id: String,
        /*
        * флаг явки сотрудника
        */
        @SerializedName("worker_presence")
        val presence: Boolean,
        /*
        * время начала
        */
        @SerializedName("time_begin")
        val timeBegin: Long?,
        /*
        * время окончания
        */
        @SerializedName("time_finish")
        val timeFinish: Long?,
        /*
        * время работы
        */
        @SerializedName("work_time")
        val workTime: Long?,
        /*
        * флаг ночной смены
        */
        @SerializedName("work_night")
        val workNight: Boolean?,
        /*
        * время работы ночью
        */
        @SerializedName("time_night")
        val timeNight: Long?
)