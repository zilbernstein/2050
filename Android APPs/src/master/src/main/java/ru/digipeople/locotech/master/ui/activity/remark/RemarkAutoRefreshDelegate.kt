package ru.digipeople.locotech.master.ui.activity.remark

import android.os.Handler
import java.util.*

/**
 * Делегат автоматического обновления замечаний
 * @author Kashonkov Nikita
 */
class RemarkAutoRefreshDelegate(val handler: Handler) {

    private lateinit var autoRefresRunnable: Runnable
    var listener: ((timeDelta: Long)-> Unit)? = null
    private var lastTimeUpdate = Date()
    /**
     * Инициализация
     */
    init {
        autoRefresRunnable = Runnable()
        {
            val newTimeUpdate = Date()
            val timeDelta = newTimeUpdate.time - lastTimeUpdate.time
            lastTimeUpdate = newTimeUpdate
            listener?.invoke(timeDelta)
            /**
             * установка задержки 1с
             */
            handler.postDelayed(autoRefresRunnable, 1000)
        }
    }
    /**
     * Действия при старте
     */
    fun onStart() { handler.postDelayed(autoRefresRunnable, 1000) }
    /**
     * Действия при остановке
     */
    fun onStop() { handler.removeCallbacks(autoRefresRunnable) }

}