package ru.digipeople.locotech.master.ui.activity.equipment

import android.os.Handler
import ru.digipeople.locotech.master.ui.activity.equipment.adapter.EquipmentAdapter

/**
 * Делегат обратного отсчета для списка локомотивов
 *
 * @author Kashonkov Nikita
 */
class EquipmentAutorefreshDelegate(val handler: Handler, val adapter: EquipmentAdapter) {

    private lateinit var autoRefresRunnable: Runnable

    init {
        autoRefresRunnable = Runnable()
        {
            /**
             * обновление данных в адаптере, установка задержки в 1с
             */
            adapter.notifyAdapterDataSetChanged()
            handler.postDelayed(autoRefresRunnable, 1000)
        }
    }
    /**
     * операции при старте
     */
    fun onStart() {
        handler.postDelayed(autoRefresRunnable, 1000)
    }
    /**
     * операции при остановке
     */
    fun onStop() {
        handler.removeCallbacks(autoRefresRunnable)
    }

}