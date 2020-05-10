package ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.technologist.R
import ru.digipeople.utils.android.AndroidUtils

/**
 * Дивайдер для адаптера замечания
 *
 * @author Aleksandr Brazhkin
 */
class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val separator = ColorDrawable(ContextCompat.getColor(context, R.color.appGray))
    private val size = AndroidUtils.dpToPx(1f, context).toInt()
    /**
     * Получение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = size
        }
        super.getItemOffsets(outRect, view, parent, state)
    }
    /**
     * оформление элемента списка
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (parent.getChildAdapterPosition(child) != 0) {
                separator.setBounds(child.left, child.top - size, child.right, child.top)
                separator.draw(c)
            }
        }
    }
}