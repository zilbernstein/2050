package ru.digipeople.ui.adapter.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Decoration for adapter's items which are represented by grid row
 * @param thickness - thickness of divider in pixels
 * @param viewTypeSelector - selector for viewTypes which should be decorated
 */
class GridDividerItemDecoration(
        private val thickness: Float,
        private val viewTypeSelector: ((viewType: Int) -> Boolean)? = null
) : RecyclerView.ItemDecoration() {
    private val linePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = thickness
    }
    /**
     * отрисовка
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()
        for (i in 0 until parent.childCount) {
            val viewItem = parent.getChildAt(i)
            if (shouldDecorate(parent, viewItem)) {
                drawHorizontal(c, parent, viewItem)
                if (viewItem is ViewGroup) {
                    val containerLayout = viewItem.getChildAt(0) as ViewGroup? ?: return
                    for (j in 0 until containerLayout.childCount) {
                        drawVertical(c, viewItem, containerLayout.getChildAt(j))
                    }
                }
            }
        }
        c.restore()
    }
    /**
     * отрисовка вертикальных элементов
     */
    private fun drawVertical(c: Canvas, parent: ViewGroup, view: View) {
        val top = parent.top.toFloat()
        val bottom = top + parent.height.toFloat()
        val left = parent.left + view.left.toFloat()
        val right = left + view.width.toFloat()

        c.drawLine(left, top, left, bottom, linePaint)
        c.drawLine(right, top, right, bottom, linePaint)
    }
    /**
     * отрннисовка горизонтальных элементов
     */
    private fun drawHorizontal(c: Canvas, parent: RecyclerView, view: View) {
        val top = view.top.toFloat()
        val bottom = top + view.height.toFloat()
        val left = view.left.toFloat()
        val right = left + view.width.toFloat()

        c.drawLine(left, top, right, top, linePaint)
        c.drawLine(left, bottom, right, bottom, linePaint)
    }

    private fun shouldDecorate(parent: RecyclerView, view: View): Boolean {
        if (viewTypeSelector == null) return true
        val position = parent.getChildAdapterPosition(view)
        if (position == -1) return false
        return parent.adapter?.let { adapter ->
            if (position >= adapter.itemCount) return@let false
            viewTypeSelector.invoke(adapter.getItemViewType(position))
        } ?: throw IllegalStateException("Recycler view isn't attached to it's own adapter")
    }
}