package ru.digipeople.locotech.master.model

/**
 * Модель ТМЦ
 *
 * @author Kashonkov Nikita
 */
open class TMC {
    /**
     * ID ТМЦ
     */
    lateinit var id: String
    /**
     * Название ТМЦ
     */
    lateinit var name: String
    /**
     * Остаток ТМЦ на складе
     */
    var stockRest: Double = 0.0
    /**
     * Остаток ТМЦ на участке
     */
    var workshop: Double = 0.0
    /**
     * Единица измерения
     */
    lateinit var uom: String
}