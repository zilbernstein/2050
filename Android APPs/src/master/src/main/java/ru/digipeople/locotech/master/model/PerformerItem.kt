package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель исполнителя
 *
 * @author Kashonkov Nikita
 */
class PerformerItem {
    /**
     * Исполнитель
     */
    lateinit var performer: Performer
    /**
     * Флаг того что в работе
     */
    var isInWork = false
    /**
     * Флаг того что в новой работе
     */
    var isInNewWork = false
    /**
     * Процент загрузки
     */
    var loadPercent = 0.0
    /**
     * Процент загрузки
     */
    var newLoadPercent = 0.0
    /**
     * Длина смены
     */
    var shiftDuration = 0.0

}