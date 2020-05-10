package ru.digipeople.locotech.inspector.ui.activity.equipment

import android.os.Handler
import ru.digipeople.locotech.inspector.ui.activity.equipment.adapter.EquipmentAdapter
/**
 * Класс необходимый для обновления данных локомотивов
 */
class EquipmentAutoRefreshDelegate(val handler: Handler, val adapter: EquipmentAdapter) {

    private lateinit var autoRefresRunnable: Runnable
    /**
     * Инициализация
     */
    init {
        autoRefresRunnable = Runnable()
        {
            adapter.notifyAdapterDataSetChanged()
            /**
             * Установка задержки 1с
             */
            handler.postDelayed(autoRefresRunnable, 1000)
        }
    }
    /**
     * Старт
     */
    fun onStart() {
        handler.postDelayed(autoRefresRunnable, 1000)
    }
    /**
     * Остновка
     */
    fun onStop() {
        handler.removeCallbacks(autoRefresRunnable)
    }

}