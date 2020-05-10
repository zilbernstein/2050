package ru.digipeople.ui.view

import android.content.Context
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ru.digipeople.ui.R

/**
 * Горизонтальный SortView
 *
 * @author Kashonkov Nikita
 */
class HorizontalSortView : BaseSortView {
    /**
     * Конструкторы
     */
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context) : this(context, null)

    override fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.sort_view, this, true)
                .apply {
                    val drawableStart = findViewById<ImageView>(R.id.sort_drawable_start)
                    drawableStart.setImageDrawable(startDrawable)
                }
    }
}