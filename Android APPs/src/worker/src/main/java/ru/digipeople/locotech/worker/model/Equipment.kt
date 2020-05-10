package ru.digipeople.locotech.worker.model

/**
 * Модель оборудования
 *
 * @author Kashonkov Nikita
 */
class Equipment {
    /**
     * Id оборудования
     */
    lateinit var equipmentId: String
    /**
     * Отображаесое название
     */
    lateinit var equipmentName: String
    /**
     * Процент выполнения работ
     */
    var equipmentProgress: Int = 0
    /**
     * Список сеций
     */
    var sectionList: List<Section> = emptyList()
}