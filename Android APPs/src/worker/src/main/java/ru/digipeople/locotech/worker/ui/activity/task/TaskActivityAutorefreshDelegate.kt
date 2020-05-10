package ru.digipeople.locotech.worker.ui.activity.task

import android.os.Handler
import java.util.*

/**
 * Делегат обратного отсчета для экрана с задачами
 *
 * @author Kashonkov Nikita
 */
class TaskActivityAutorefreshDelegate(val handler: Handler) {

    private lateinit var autoRefresRunnable: Runnable
    var listener: ((timeDelta: Long)-> Unit)? = null
    private var lastTimeUpdate = Date()
    /**
     * инициализация
     */
    init {
        autoRefresRunnable = Runnable()
        {
            val newTimeUpdate = Date()
            val timeDelta = newTimeUpdate.time - lastTimeUpdate.time
            lastTimeUpdate = newTimeUpdate
            listener?.invoke(timeDelta)
            /**
             * установка задержки в 1с
             */
            handler.postDelayed(autoRefresRunnable, 1000)
        }
    }

    fun onStart() { handler.postDelayed(autoRefresRunnable, 1000) }

    fun onStop() { handler.removeCallbacks(autoRefresRunnable) }

}