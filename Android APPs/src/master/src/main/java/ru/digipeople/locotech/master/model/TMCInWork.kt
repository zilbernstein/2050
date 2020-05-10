package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель ТМЦ в работе
 *
 * @author Kashonkov Nikita
 */
class TMCInWork : TMC() {
    /**
     * Статус
     */
    lateinit var status: TMCStatus
    /**
     * Получатель
     */
    var reciver: Performer? = null
    /**
     * Количество ТМЦ запрошенное
     */
    var requested = 0.0
    /**
     * Количество ТМЦ по норме
     */
    var normal = 0.0
    /**
     * Установлено к списанию
     */
    var assigned = 0.0
    /**
     * Минимально
     */
    var min = 0.0
    /**
     * Максимально
     */
    var max = 0.0
    /**
     * МНК
     */
    var mnk = 0.0
}