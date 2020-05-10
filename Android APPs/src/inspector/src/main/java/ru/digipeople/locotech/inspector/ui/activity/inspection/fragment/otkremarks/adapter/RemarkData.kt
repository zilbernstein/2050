package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter

import ru.digipeople.locotech.inspector.model.Remark
/**
 * Вспомогательный класс адаптера замечаний ОТК
 *
 * @author Kashonkov Nikita
 */
class RemarkData {
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
    lateinit var remark: Remark
    /**
     * Доступность кнопки удалить
     */
    var isDeleteBtnEnable: Boolean = false
}