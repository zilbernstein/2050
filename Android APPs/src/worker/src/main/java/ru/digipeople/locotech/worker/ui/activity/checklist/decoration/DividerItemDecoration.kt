package ru.digipeople.locotech.worker.ui.activity.checklist.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор для итема в списке
 *
 * @author Sostavkin Grisha
 */
class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset = AndroidUtils.dpToPx(8f, context).toInt()
    private val leftRightOffset = AndroidUtils.dpToPx(16f, context).toInt()
    /**
     * Установка отступов при оформлении элементов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = offset
        }
        outRect.bottom = offset
        outRect.left = leftRightOffset
    }
}