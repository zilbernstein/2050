package ru.digipeople.locotech.worker.model

/**
 * Модель ТМЦ в работе
 *
 * @author Kashonkov Nikita
 */
class TMCInWork {
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
     * Количество ТМЦ запрошенное
     */
    var tmcRequested = 0.0
    /**
     * Количество ТМЦ по норме
     */
    var tmcNormal = 0.0
    /**
     * Единица измерения
     */
    var uom: String = ""
}