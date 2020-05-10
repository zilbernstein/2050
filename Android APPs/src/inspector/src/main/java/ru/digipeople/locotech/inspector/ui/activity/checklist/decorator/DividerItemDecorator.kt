package ru.digipeople.locotech.inspector.ui.activity.checklist.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.CheckListAdapter
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор для чеклиста
 * @author Kashonkov Nikita
 */
class DividerItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {
    private lateinit var data: AdapterData

    private val workOffset = AndroidUtils.dpToPx(1f, context).toInt()
    private val remarkOffset = AndroidUtils.dpToPx(16f, context).toInt()
    /**
     * получение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        data = (parent.adapter as CheckListAdapter).adapterData
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (data.isOperation(position)) {
            if (position == data.lastIndex) {
                outRect.bottom = remarkOffset
                return
            }
            if (data.isOperation(position + 1)) {
                outRect.bottom = workOffset
            } else if (data.isEquipmentCsoData(position + 1)) {
                outRect.bottom = remarkOffset
            }
        } else if (data.isEquipmentCsoData(position)) {
            if (position == data.lastIndex) {
                outRect.bottom = remarkOffset
                return
            }
            if (data.isEquipmentCsoData(position + 1)) {
                outRect.bottom = remarkOffset
                return
            }
        }
    }
}