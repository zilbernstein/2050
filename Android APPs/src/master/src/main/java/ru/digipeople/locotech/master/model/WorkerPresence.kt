package ru.digipeople.locotech.master.model

import java.util.*
/**
 * Модель явки сотрудника
 */
class WorkerPresence {
    /**
     * Идентификатор сотрудника
     */
    var id: String = ""
    /**
     * Фамилия+инициалы работника
     */
    var fio: String = ""
    /**
     * Номер табеля сотрудника
     */
    var tabel: String = ""
    /**
     * Время прихода на работу, миллисекунды в UTC
     */
    var timeIn: Date? = null
    /**
     * Время ухода с работы, миллисекунды в UTC
     */
    var timeOut: Date? = null
    /**
     * Время начала работы, миллисекунды в UTC
     */
    var timeBegin: Date? = null
    /**
     * Время окончания работы, миллисекунды в UTC
     */
    var timeFinish: Date? = null
    /**
     * Часы работы (длительность), в миллисекундах (значение проставленное Мастером)
     */
    var workTime: Date? = null
    /**
     * Явка на работу
     */
    var presence: Boolean = false
    /**
     * Отметка ночной смены
     */
    var night: Boolean = false
    /**
     * Длительность работы ночью с 00:00, в миллисекундах(значение проставленное Мастером)
     */
    var timeNight: Date? = null
}