package ru.digipeople.locotech.inspector.ui.activity.checklist.adapter

import ru.digipeople.locotech.inspector.model.EquipmentCso

/**
 * Вспомогательный класс для работы с адаптером чеклиста
 * @author Kashonkov Nikita
 */
class EquipmentCSOData {
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
     * Модель оборудования Контрольно-Серверной операции
     */
    lateinit var equipment: EquipmentCso
}