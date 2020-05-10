package ru.digipeople.locotech.core.ui.widget

import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener

/**
 * ClickListener c задержкой
 */
class DelayedClickListener(private val delegate: OnClickListener) : OnClickListener {
    /**
     * 0,3с
     */
    private val delay: Long = 300

    private var lastClickTime: Long = 0

    override fun onClick(v: View?) {
        val now = SystemClock.uptimeMillis()
        val diff = now - lastClickTime
        if (diff > delay) {
            lastClickTime = now
            delegate.onClick(v)
        }
    }
}