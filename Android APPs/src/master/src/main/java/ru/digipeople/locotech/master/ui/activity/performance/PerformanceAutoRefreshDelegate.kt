package ru.digipeople.locotech.master.ui.activity.performance

import android.os.Handler
import ru.digipeople.locotech.master.ui.activity.performance.adapter.PerformanceAdapter

/**
 * Делегат обратного отсчета для списка локомотивов.
 */
class PerformanceAutoRefreshDelegate(private val handler: Handler, private val adapter: PerformanceAdapter) {

    private lateinit var autoRefreshRunnable: Runnable

    init {
        autoRefreshRunnable = Runnable {
            adapter.updateTime()
            /**
             * установка задержки 1с
             */
            handler.postDelayed(autoRefreshRunnable, 1000)
        }
    }
    /**
     * действия при старте
     */
    fun onStart() {
        handler.postDelayed(autoRefreshRunnable, 1000)
    }
    /**
     * действия при остановке
     */
    fun onStop() {
        handler.removeCallbacks(autoRefreshRunnable)
    }

}