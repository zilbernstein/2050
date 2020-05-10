package ru.digipeople.locotech.worker.model

/**
 * Модель пункта чеклиста
 *
 * @author Sostavkin Grisha
 */
class ChecklistItem {
    /**
     * Идентификатор строки чеклиста
     */
    var id = ""
    /**
     * Наименование строки чеклиста
     */
    var name = ""
    /**
     * Ссылка строки чеклиста
     */
    var link = ""
    /**
     * Признак проверки строки чеклиста
     */
    var isChecked = false
}