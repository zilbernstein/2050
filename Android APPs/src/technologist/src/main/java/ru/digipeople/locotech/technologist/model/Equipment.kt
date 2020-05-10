package ru.digipeople.locotech.technologist.model

/**
 * Модель оборудования
 *
 * @author Sostavkin Grisha
 */
class Equipment {
    /**
     * Название локомотива
     */
    lateinit var name: String
    /**
     * Список секций
     */
    lateinit var section: List<Section>
}