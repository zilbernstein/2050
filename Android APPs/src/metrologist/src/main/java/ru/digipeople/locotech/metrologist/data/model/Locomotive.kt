package ru.digipeople.locotech.metrologist.data.model
/**
 * Модель локомотива
 */
class Locomotive {
    /**
     * Id локомотива
     */
    var id = ""
    /**
     * Номер локомотива
     */
    var number = ""
    /**
     * Субомер локомотива
     */
    var subNumber = ""
    /**
     * Бортовой номер
     */
    var name = ""
    /**
     * Список секций
     */
    var sections = emptyList<Section>()
}