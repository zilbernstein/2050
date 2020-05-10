package ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig

/**
 * Вспомогательный класс адаптера списка бригад и исполнителей
 *
 * @author Sostavkin Grisha
 */
class BrigadeAdapterData : ArrayList<Any>() {
    /**
     * проверка что элемент - исполнитель
     */
    fun isWorkerView(position: Int): Boolean {
        val item = get(position)
        return item is WorkerView
    }
    /**
     * получение элемента исполнителя по id
     */
    fun getWorkerView(position: Int): WorkerView {
        return get(position) as WorkerView
    }
    /**
     * проверка что элемент - бригада
     */
    fun isBrigadeView(position: Int): Boolean {
        val item = get(position)
        return item is BrigView
    }
    /**
     * получение элемента бригады  по id
     */
    fun getBrigadeView(position: Int): BrigView {
        return get(position) as BrigView
    }
}