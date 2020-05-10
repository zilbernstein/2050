package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter

/**
 * Вспомогательный класс адаптера цикловых работ
 *
 * @author Kashonkov Nikita
 */
class AdapterData : ArrayList<Any>() {
    /**
     * Проверка, что элемент - группа работ
     */
    fun isCyclicGroup(position: Int): Boolean {
        val item = get(position)
        return item is CyclicGroupData
    }
    /**
     * Получить группу работ
     */
    fun getCyclicGroup(position: Int): CyclicGroupData {
        return get(position) as CyclicGroupData
    }
    /**
     * Проверка, чт элемент - работа
     */
    fun isWork(position: Int): Boolean {
        val item = get(position)
        return item is WorkData
    }
    /**
     * Получить работу
     */
    fun getWorkData(position: Int): WorkData {
        return get(position) as WorkData
    }
    /**
     * Проверка что элемент - разделитель
     */
    fun isWorkSplash(position: Int): Boolean {
        val item = get(position)
        return item is WorkSplash
    }
}