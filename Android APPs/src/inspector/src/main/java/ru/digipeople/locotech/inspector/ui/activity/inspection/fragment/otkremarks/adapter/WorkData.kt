package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter

import ru.digipeople.locotech.inspector.model.Work

/**
 * Вспомогательный класс адаптера замечаний ОТК
 * Работа в замечании ОТК
 *
 * @author Kashonkov Nikita
 */
class WorkData {
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
     * Ссылка на замечание к котороу относится данная работа
     */
    lateinit var remarkData: RemarkData
}