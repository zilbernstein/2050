package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.RemarkRzdAdapter
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
class DividerItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {
    private lateinit var data: AdapterData

    private val workOffset = AndroidUtils.dpToPx(1f, context).toInt()
    private val remarkOffset = AndroidUtils.dpToPx(16f, context).toInt()
    /**
     * получение значений отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        data = (parent.adapter as RemarkRzdAdapter).adapterData
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (data.isWork(position)) {
            if (position == data.lastIndex) {
                outRect.bottom = remarkOffset
                return
            }
            if (data.isWork(position + 1)) {
                outRect.bottom = workOffset
            } else if (data.isRemark(position + 1)) {
                outRect.bottom = remarkOffset
            }
        }

        if (data.isWorkSplash(position)) {
            outRect.bottom = remarkOffset
        }
    }
}