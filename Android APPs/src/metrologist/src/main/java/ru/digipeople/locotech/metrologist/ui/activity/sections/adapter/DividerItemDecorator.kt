package ru.digipeople.locotech.metrologist.ui.activity.sections.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import ru.digipeople.utils.android.AndroidUtils
/**
 * Декоратор секций
 */
class DividerItemDecorator(context: Context) : ItemDecoration(){
    private val offset = AndroidUtils.dpToPx(4f, context).toInt()
    /**
     * Получение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = offset
    }
}