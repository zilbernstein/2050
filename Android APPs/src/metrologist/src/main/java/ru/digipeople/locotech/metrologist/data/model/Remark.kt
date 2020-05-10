package ru.digipeople.locotech.metrologist.data.model

import ru.digipeople.locotech.core.data.model.Performer
import java.util.*

/**
 * Модель замечания
 *
 * @author Michael Solenov
 */
class Remark {
    /**
     * идентификатор замечания из справочника
     */
    var id: String = ""

    /**
     * название замечания
     */
    var name: String = ""

    /**
     * дата и время создания
     */
    lateinit var date: Date

    /**
     * автор замечания
     */
    lateinit var creator: Performer

    /**
     * полное название и номер секции и локомотива
     */
    var equipmentFullName: String = ""

    /**
     * идентификатор КП
     */
    var wheelPairId: String = ""

    /**
     * номер бандажа
     */
    var flangeNumber: String = ""

    /**
     * идентификатор источника замечаний
     */
    var sourceId: String = ""

    /**
     * наименование источника замечаний
     */
    var sourceName: String = ""
}