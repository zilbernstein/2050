package ru.digipeople.locotech.worker.model

/**
 * Модель чеклист
 *
 * @author Sostavkin Grisha
 */
class Checklist {
    /**
     * Список чеклиста
     */
    var items = emptyList<ChecklistItem>()
    /**
     * Ссылка на пдф файл
     */
    var techProcessUrl = ""
}