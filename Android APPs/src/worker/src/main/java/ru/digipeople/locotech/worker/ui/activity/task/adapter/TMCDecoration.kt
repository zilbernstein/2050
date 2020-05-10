package ru.digipeople.locotech.worker.ui.activity.task.adapter

import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * Декоратор для ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCDecoration constructor(val divider: Drawable) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val count = parent.childCount - 2
        for (i in 0..count) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            /**
             * Установка параметров разделителей
             */
            val dividerTop = child.bottom + params.bottomMargin;
            val dividerBottom = dividerTop + divider.intrinsicHeight;

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(c);
        }
    }
}