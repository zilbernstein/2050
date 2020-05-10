package ru.digipeople.locotech.master.ui.activity.workerspresence.adapter

import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.BrigadePresenceItem
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.WorkerPresenceItem
/**
 * Вспомогательный класс для адаптера явки сотрудников
 */
class AdapterData(items: List<Any> = emptyList()) : ArrayList<Any>(items) {
    /**
     * Проверка, что элемент -  бирагада
     */
    fun isBrigadePresence(position: Int): Boolean {
        val item = get(position)
        return item is BrigadePresenceItem
    }
    /**
     * Получение бригады
     */
    fun getBrigadePresence(position: Int): BrigadePresenceItem {
        return get(position) as BrigadePresenceItem
    }
    /**
     * Проверка. что элемент - исполнитель
     */
    fun isWorkerPresence(position: Int): Boolean {
        val item = get(position)
        return item is WorkerPresenceItem
    }
    /**
     * Получение исполнителя
     */
    fun getWorkerPresence(position: Int): WorkerPresenceItem {
        return get(position) as WorkerPresenceItem
    }
}