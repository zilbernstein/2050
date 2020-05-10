package ru.digipeople.locotech.metrologist.ui.activity.profilometers.adapter

import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.Profilometer
/**
 * Вспомогательный класс для адаптера профилометров
 */
class AdapterData() : ArrayList<Any>() {
    /**
     * Проверка, что элемент - замер
     */
    fun isMeasurement(position: Int): Boolean {
        val item = get(position)
        return item is Measurement
    }
    /**
     * Получение замера
     */
    fun getMeasurement(position: Int): Measurement {
        return get(position) as Measurement
    }
    /**
     * Получение профилометра
     */
    fun getProfilometer(position: Int): Profilometer {
        return get(position) as Profilometer
    }
}