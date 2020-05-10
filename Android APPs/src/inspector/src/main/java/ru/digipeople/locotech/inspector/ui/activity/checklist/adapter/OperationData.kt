package ru.digipeople.locotech.inspector.ui.activity.checklist.adapter

import ru.digipeople.locotech.inspector.model.ControlServiceOperation

/**
 * Вспомогательный класс для работы с адаптером чеклиста
 *
 * @author Kashonkov Nikita
 */
class OperationData {
    /**
     * Сущность Контрольно-Серверной операции
     */
    lateinit var operation: ControlServiceOperation
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
     * Ссылка на оборудование КСО
     */
    lateinit var equipment: EquipmentCSOData
}