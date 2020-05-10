package ru.digipeople.locotech.inspector.ui.activity.measurement.adapter

/**
 * Вспомогательный класс адаптера Замеров
 * @author Sostavkin Grisha
 */
class AdapterData: ArrayList<Any>() {
    /**
     * Проверка, что элемент - заголовок
     */
    fun isTitleView(position: Int): Boolean {
        val item = get(position)
        return item is TitleModel
    }
    /**
     * получение заголовка
     */
    fun getTitleView(position: Int): TitleModel {
        return get(position) as TitleModel
    }
    /**
     * проверка, что элемент - замер
     */
    fun isMeasurementView(position: Int): Boolean {
        val item = get(position)
        return item is MeasurementModel
    }
    /**
     * получение замера
     */
    fun getMeasurementView(position: Int): MeasurementModel {
        return get(position) as MeasurementModel
    }
    /**
     * проверка, что элемент - разделитель
     */
    fun isDividerView(position: Int): Boolean{
        val item = get(position)
        return item is DividerModel
    }
}