package ru.digipeople.locotech.master.api.model.response

import com.google.gson.annotations.SerializedName
/**
 * Модель ответа изменения явки сотрудника
 */
class ChangePresenceResponse(
        /**
         * Идентификатор сотрудника
         */
        @SerializedName("worker_id")
        val id: String,
        /**
         * Явка на работу
         */
        @SerializedName("worker_presence")
        val presence: Boolean,
        /**
         * Время прихода на работу, миллисекунды в UTC
         */
        @SerializedName("time_in")
        val timeIn: Long,
        /**
         * Время ухода с работы, миллисекунды в UTC
         */
        @SerializedName("time_out")
        val timeOut: Long,
        /**
         * Время начала работы, миллисекунды в UTC
         */
        @SerializedName("time_begin")
        val timeBegin: Long,
        /**
         * Время окончания работы, миллисекунды в UTC
         */
        @SerializedName("time_finish")
        val timeFinish: Long,
        /**
         * Часы работы (длительность) в миллисекундах
         */
        @SerializedName("work_time")
        val workTime: Long,
        /**
         * Отметка ночной смены
         */
        @SerializedName("work_night")
        val night: Boolean,
        /**
         * Длительность работы ночью с 00:00 в миллисекундах
         */
        @SerializedName("time_night")
        val timeNight: Long
)