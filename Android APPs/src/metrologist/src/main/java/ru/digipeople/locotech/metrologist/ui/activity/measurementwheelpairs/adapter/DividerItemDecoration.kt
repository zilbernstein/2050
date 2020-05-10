package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.utils.android.AndroidUtils
/**
 * Декоратор элемента ввода данных замера
 */
class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val offset = AndroidUtils.dpToPx(8f, context).toInt()
    /**
     * получение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = offset
        }
    }
}