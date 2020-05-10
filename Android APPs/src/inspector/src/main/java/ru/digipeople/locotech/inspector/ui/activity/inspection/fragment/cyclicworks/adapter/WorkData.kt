package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter

import ru.digipeople.locotech.inspector.model.Work

/**
 * Цикловая работа
 * @author Kashonkov Nikita
 */
class WorkData{
    /**
     * Сущность работы
     */
    lateinit var work: Work
    /**
     * Позиция рботы в списка работ замечания
     */
    var position: Int = -1
    /**
     * Доступность флага выбора ОТК
     */
    var isOtkCheckEnable: Boolean = false
    /**
     * Доступность флага выбора РЖД
     */
    var isRzdCheckEnable: Boolean = false
    /**
     * Ссылка на цикловую группу
     */
    lateinit var group: CyclicGroupData
}