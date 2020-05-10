package ru.digipeople.locotech.inspector.model
/**
 * Модель замечания РЖД
 */
class RemarkRzdInfo {
    /**
     * Название оборудования
     */
    lateinit var equipmentTitle: String
    /**
     * Список замечаний
     */
    lateinit var remarkList: List<Remark>
}