package ru.digipeople.locotech.worker.ui.activity.mytask.adapter

import android.view.View
import ru.digipeople.locotech.worker.model.Equipment
import ru.digipeople.locotech.worker.model.Section
import ru.digipeople.locotech.worker.model.Work

/**
 * Вспомогательный класс для работы с адаптером работы
 *
 * @author Kashonkov Nikita
 */
class AdapterData : ArrayList<Any>() {
    /**
     * Провекрка, что элемент - секция
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
     * Проверка. что элемент - работа
     */
    fun isWork(position: Int): Boolean {
        val item = get(position)
        return item is Work
    }
    /**
     * Получение работы
     */
    fun getWork(position: Int): Work {
        return get(position) as Work
    }
    /**
     * Проверка, что элемент - разделитель
     */
    fun isDividerView(position: Int): Boolean {
        val item = get(position)
        return item is View
    }
    /**
     * Получение разделителя
     */
    fun getDividerView(position: Int): View {
        return get(position) as View
    }
    /**
     * Проверка, что элемент - оборудование
     */
    fun isEquipment(position: Int): Boolean {
        val item = get(position)
        return item is Equipment
    }
    /**
     * Получение оборудования
     */
    fun getEquipment(position: Int): Equipment{
        return get(position) as Equipment
    }
    /**
     * Проверка, что элемент - заголовок
     */
    fun isTitle(position: Int): Boolean{
        val item = get(position)
        return item is TitleItem
    }
    /**
     * Получение заголовка
     */
    fun getTitleItem(position: Int): TitleItem {
        return  get(position) as TitleItem
    }
}