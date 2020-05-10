package ru.digipeople.locotech.metrologist.ui.activity.sections.adapter

import ru.digipeople.locotech.metrologist.data.model.Locomotive
import ru.digipeople.locotech.metrologist.data.model.Section

/**
 * Вспомогательный класс адаптера секций
 */
class AdapterData(items: List<Any>) : ArrayList<Any>(items) {
    /**
     * Проверка, что элемент - секция
     */
    fun isSection(position: Int): Boolean {
        val item = get(position)
        return item is Section
    }
    /**
     * Получение секции
     */
    fun getSection(position: Int): Section {
        return get(position) as Section
    }
    /**
     * Проврека. что элемент - локомотив
     */
    fun isLocomotive(position: Int): Boolean {
        val item = get(position)
        return item is Locomotive
    }
    /**
     * Получение локомотива
     */
    fun getLocomotive(position: Int): Locomotive {
        return get(position) as Locomotive
    }
}