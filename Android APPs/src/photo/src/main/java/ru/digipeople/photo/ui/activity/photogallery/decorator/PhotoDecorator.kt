package ru.digipeople.photo.ui.activity.photogallery.decorator

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.utils.android.AndroidUtils

/**
 * Декоратор для адаптера фото
 *
 * @author Kashonkov Nikita
 */
class PhotoDecorator(context: Context,
                     offsetDp: Float) : RecyclerView.ItemDecoration() {
    private val offset = AndroidUtils.dpToPx(offsetDp, context).toInt()
    /**
     * установка отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (position == 0) {
            outRect.left = offset
        }
        outRect.right = offset
    }
}