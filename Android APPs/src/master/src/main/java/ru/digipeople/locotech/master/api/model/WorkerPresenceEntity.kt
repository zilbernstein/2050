package ru.digipeople.locotech.master.api.model

import com.google.gson.annotations.SerializedName
/**
 * Модель явки сотрудник
 */
class WorkerPresenceEntity(
        /**
         * Идентификатор сотрудника
         */
        @SerializedName("worker_id")
        val id: String,
        /**
         * Фамилия+инициалы работника
         */
        @SerializedName("worker_fio")
        val fio: String,
        /**
         * Номер табеля сотрудника
         */
        @SerializedName("worker_tabel")
        val tabel: String,
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
         * Часы работы (длительность), в миллисекундах (значение проставленное Мастером)
         */
        @SerializedName("work_time")
        val workTime: Long,
        /**
         * Явка на работу
         */
        @SerializedName("worker_presence")
        val presence: Boolean,
        /**
         * Отметка ночной смены
         */
        @SerializedName("worker_night")
        val night: Boolean,
        /**
         * Длительность работы ночью с 00:00, в миллисекундах(значение проставленное Мастером)
         */
        @SerializedName("time_night")
        val timeNight: Long
)