package ru.digipeople.locotech.master.ui.activity.workerspresence.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import ru.digipeople.locotech.master.R
import ru.digipeople.utils.android.AndroidUtils
/**
 * Класс для отображения хедера таблицы явки сотрудников
 */
//TODO delegate all responsibility to inner view, add wrapped view concept as Decoration does
class CustomTableHeader @JvmOverloads constructor(
        context: Context, attrs: AttributeSet,
        defStyleRes: Int = 0
) : CardView(context, attrs, defStyleRes) {
    private val outBounds = Rect()
    private val rectPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = AndroidUtils.dpToPx(1f, context)
    }

    private val headerView: View
    /**
     * Инициализация параметров
     */
    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomTableHeader, defStyleRes, 0)
                .apply {
                    val view = View.inflate(context, getResourceId(R.styleable.CustomTableHeader_layout, 0), this@CustomTableHeader)
                    headerView = (view as ViewGroup).getChildAt(0) //take table markup layout
                }
    }
    /**
     * Стандартная ф-я рисования
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGridDividers(canvas)
    }
    /**
     * отрисовка разделителей
     */
    private fun drawGridDividers(canvas: Canvas) {
        val container = headerView as ViewGroup? ?: return
        for (i in 0 until container.childCount) {
            if (i == 0 || i == container.childCount - 1) continue
            val view = container.getChildAt(i)
            outBounds.apply {
                top = view.top + view.paddingTop
                bottom = top + view.height + view.paddingBottom
                left = container.left + view.left + view.paddingLeft
                right = left + view.width + view.paddingRight
            }
            canvas.drawRect(outBounds, rectPaint)
        }
    }
}