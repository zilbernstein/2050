package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.adapter.RemarkOtkAdapter
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор замечаний ОТК
 *
 * @author Kashonkov Nikita
 */
class DividerItemDecorator(private val context: Context) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
    private lateinit var data: AdapterData

    private val workOffset = 1f
    private val remarkOffset = 16f

    private var workOffsetPx = 0f
    private var remarkOffsetPx = 0f

    init {
        convertOffsets()
    }
    /**
     * получение значений отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        data = (parent.adapter as RemarkOtkAdapter).adapterData
        val position = (view.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams).viewAdapterPosition

        if (data.isWork(position)) {
            if (position == data.lastIndex) {
                outRect.bottom = remarkOffsetPx.toInt()
                return
            }
            if (data.isWork(position + 1)) {
                outRect.bottom = workOffsetPx.toInt()
            } else if (data.isRemark(position + 1)) {
                outRect.bottom = remarkOffsetPx.toInt()
            }
        }

        if (data.isWorkSplash(position)) {
            outRect.bottom = remarkOffsetPx.toInt()
        }
    }
    /**
     * преобразование отступов
     */
    private fun convertOffsets() {
        val res = context.resources
        workOffsetPx = AndroidUtils.dpToPx(workOffset, context)
        remarkOffsetPx = AndroidUtils.dpToPx(remarkOffset, context)

    }
}