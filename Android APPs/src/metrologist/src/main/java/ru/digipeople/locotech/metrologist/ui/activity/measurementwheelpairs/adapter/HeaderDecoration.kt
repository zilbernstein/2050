package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
/**
 * Декоратор заголовка ввода данных замера
 */
class HeaderDecoration(recyclerView: RecyclerView, offset: Int) : RecyclerView.ItemDecoration() {
    private val headerView: View
    private val offset = offset

    init {
        val layoutInflater = recyclerView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        headerView = layoutInflater.inflate(R.layout.item_wheel_param_header, recyclerView, false)
    }
    /**
     * получение отступов
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        configureHeaderView(parent)
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.top = headerView.measuredHeight
        }
    }
    /**
     * Отрисовка
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        headerView.draw(c)
    }

    private fun configureHeaderView(parent: RecyclerView) {
        if (headerView.measuredWidth != 0) {
            return
        }
        headerView.measure(
                //crutch - parent.wight = размер экрана
                View.MeasureSpec.makeMeasureSpec(parent.measuredWidth - (offset * 2), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    }
}