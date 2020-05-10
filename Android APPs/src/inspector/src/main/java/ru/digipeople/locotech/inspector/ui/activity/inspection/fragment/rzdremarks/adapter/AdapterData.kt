package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter

/**
 * Вспомогательный класс адаптера замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
class AdapterData : ArrayList<Any>() {
    /**
     * Проверка, что элемент - замечание
     */
    fun isRemark(position: Int): Boolean {
        val item = get(position)
        return item is RemarkData
    }
    /**
     * получить замечание
     */
    fun getRemark(position: Int): RemarkData {
        return get(position) as RemarkData
    }
    /**
     * Проверка, что элемент - работа
     */
    fun isWork(position: Int): Boolean {
        val item = get(position)
        return item is WorkData
    }
    /**
     * получить работу
     */
    fun getWorkData(position: Int): WorkData {
        return get(position) as WorkData
    }
    /**
     * проверка, что элемент - разделитель
     */
    fun isWorkSplash(position: Int): Boolean {
        val item = get(position)
        return item is WorkSplash
    }
}