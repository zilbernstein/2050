package ru.digipeople.locotech.worker.ui.activity.task.adapter

import android.content.Context

/**
 * LinearLayoutManager для ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCLinearLayoutManager(context: Context) : androidx.recyclerview.widget.LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}