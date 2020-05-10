package ru.digipeople.locotech.inspector.ui.activity.print.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор печати
 *
 * @author Kashonkov Nikita
 */
class DividerItemDecorator(context: Context) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private val offset = AndroidUtils.dpToPx(8f, context).toInt()
    /**
     * получить значение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        outRect.bottom = offset
    }
}