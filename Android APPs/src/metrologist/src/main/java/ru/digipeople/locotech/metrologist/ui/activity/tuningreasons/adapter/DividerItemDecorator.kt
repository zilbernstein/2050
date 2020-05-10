package ru.digipeople.locotech.metrologist.ui.activity.tuningreasons.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор выбора причины обточки
 *
 * @author Michael Solenov
 */
class DividerItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val offset = AndroidUtils.dpToPx(4f, context).toInt()
    /**
     * Получение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = offset
    }
}