package ru.digipeople.locotech.master.ui.activity.measurement.adapter

import ru.digipeople.locotech.core.data.model.Stage

/**
 * Вспомогательный класс адаптера измерений
 * @author Sostavkin Grisha
 */
class AdapterData : ArrayList<Any>() {
    /**
     * добавленеи замера
     */
    fun addMeasurements(stage: Stage, measurements: List<MeasurementModel>) {
        if (measurements.isEmpty()) return
        add(MeasurementTitleModel(stage.title))
        addAll(measurements)
        add(DividerModel)
    }
    /**
     * проверка, что элемент - заголовок
     */
    fun isMeasurementTitleView(position: Int): Boolean {
        val item = get(position)
        return item is MeasurementTitleModel
    }
    /**
     * получения заголовка по индексу
     */
    fun getMeasurementTitleView(position: Int): MeasurementTitleModel {
        return get(position) as MeasurementTitleModel
    }
    /**
     * проверка, что элемент - замер
     */
    fun isMeasurementView(position: Int): Boolean {
        val item = get(position)
        return item is MeasurementModel
    }
    /**
     * получение замера по индексу
     */
    fun getMeasurementView(position: Int): MeasurementModel {
        return get(position) as MeasurementModel
    }
    /**
     * проверка, что элемент - разделитель
     */
    fun isDividerView(position: Int): Boolean {
        val item = get(position)
        return item is DividerModel
    }
}