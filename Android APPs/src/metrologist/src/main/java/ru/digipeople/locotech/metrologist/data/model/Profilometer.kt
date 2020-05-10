package ru.digipeople.locotech.metrologist.data.model
/**
 * Модель профилометра
 */
class Profilometer {
    /**
     * Номер профилометра
     */
    lateinit var number: String
    /**
     * Список измерений
     */
    var measurementList: MutableList<Measurement> = mutableListOf()
}