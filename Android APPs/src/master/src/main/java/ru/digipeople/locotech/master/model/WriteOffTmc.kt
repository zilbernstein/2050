package ru.digipeople.locotech.master.model

import ru.digipeople.locotech.core.data.model.Performer

/**
 * Модель списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
class WriteOffTmc {
    /**
     * ID работы
     */
    lateinit var workId: String
    /**
     * Название работы
     */
    lateinit var workName: String
    /**
     * список ТМЦ
     */
    var tmcList: List<TMCInWork> = listOf()
}