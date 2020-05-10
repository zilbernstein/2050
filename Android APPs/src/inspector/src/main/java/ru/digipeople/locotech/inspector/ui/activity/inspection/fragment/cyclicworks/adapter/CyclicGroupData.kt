package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter

import ru.digipeople.locotech.inspector.model.CyclicGroup

/**
 * Вспомогательный класс адаптера цикловых работ
 * Цикловая группа
 *
 * @author Kashonkov Nikita
 */
class CyclicGroupData {
    /**
     * Количество работ принятых мастером в группе
     */
    var masterUnCheckCount = 0
    /**
     * Количество работ принятых РЖД в группе
     */
    var rzdUnCheckCount = 0
    /**
     * Количество работ принятых СЛД в группе
     */
    var sldUnCheckCount = 0
    /**
     * Доступность кнопки принять все
     */
    var isAcceptBtnEnable = false
    /**
     * Модель группы работ
     */
    lateinit var cyclicGroup: CyclicGroup
}