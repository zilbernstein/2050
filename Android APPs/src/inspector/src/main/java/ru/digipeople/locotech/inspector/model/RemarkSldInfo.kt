package ru.digipeople.locotech.inspector.model
/**
 * Модель замечания ОТК
 */
class RemarkSldInfo {
    /**
     * Название оборудования
     */
    lateinit var equipmentTitle: String
    /**
     * Список замечаний
     */
    lateinit var remarkList: List<Remark>
}