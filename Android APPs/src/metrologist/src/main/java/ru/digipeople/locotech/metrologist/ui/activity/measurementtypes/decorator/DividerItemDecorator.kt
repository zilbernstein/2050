package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор типов замеров
 *
 * @author Michael Solenov
 */
class DividerItemDecorator(context: Context) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private val offset = AndroidUtils.dpToPx(4f, context).toInt()
    /**
     * Получение значение отступа
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        outRect.bottom = offset
    }
}