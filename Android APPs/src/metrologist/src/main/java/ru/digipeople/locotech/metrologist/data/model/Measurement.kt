package ru.digipeople.locotech.metrologist.data.model

import ru.digipeople.locotech.core.data.model.Performer
import java.util.*
/**
 * Модель замера
 */
class Measurement {
    /**
     * Идентификатор замера
     */
    lateinit var id: String
    /**
     * Номер замера
     */
    lateinit var number: String
    /**
     * Название секции
     */
    lateinit var sectionName: String
    /**
     * номер секции
     */
    lateinit var sectionNumber: String
    /**
     *субномер секции
     */
    lateinit var sectionSubnumber: String
    /**
     * Название оборудования
     */
    lateinit var equipmentName: String
    /**
     * Тип замера
     */
    lateinit var type: String
    /**
     * Вид замера
     */
    lateinit var category: String
    /**
     * Дата и время замера
     */
    lateinit var date: Date
    /**
     * Признак ручного замера
     */
    var isManual: Boolean = false
    /**
     * Сотрудник
     */
    lateinit var creator: Performer
    /**
     * Номер профилометра
     */
    lateinit var profilometerNumber: String
}
