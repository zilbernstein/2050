package ru.digipeople.locotech.metrologist.helper.session

import ru.digipeople.locotech.metrologist.data.model.RepairType
/**
 * Фильтр оборудования
 */
data class EquipmentFilter(val repairTypes: List<RepairType>)