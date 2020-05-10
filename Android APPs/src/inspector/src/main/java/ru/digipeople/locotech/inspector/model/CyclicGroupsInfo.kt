package ru.digipeople.locotech.inspector.model
/**
 * Модель информации о группе цикловых работ
 */
class CyclicGroupsInfo {
    /**
     * Название оборудования
     */
    lateinit var equipmentTitle: String
    /**
     * Cписок групп
     */
    lateinit var groupList: List<CyclicGroup>
}