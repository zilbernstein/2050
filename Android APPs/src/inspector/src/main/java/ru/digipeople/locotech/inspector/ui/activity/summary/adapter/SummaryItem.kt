package ru.digipeople.locotech.inspector.ui.activity.summary.adapter

/**
 * Модель суммарной информации
 * @author Sostavkin Grisha
 */
class SummaryItem {
    /**
     * Название
     */
    lateinit var name: String
    /**
     * Принято
     */
    var accepted: Int = 0
    /**
     * Процент
     */
    var percent: Int = 0
    /**
     * Не принято РЖД
     */
    var declinedRJD: Int = 0
    /**
     * Не принято ОТК
     */
    var declinedOTK: Int = 0
}