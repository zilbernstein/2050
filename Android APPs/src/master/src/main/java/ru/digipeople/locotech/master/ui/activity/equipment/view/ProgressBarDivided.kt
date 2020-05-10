package ru.digipeople.locotech.master.ui.activity.equipment.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import ru.digipeople.locotech.master.R
import ru.digipeople.utils.android.AndroidUtils

class ProgressBarDivided @JvmOverloads constructor(
        context: Context, attributeSet: AttributeSet, defStyle: Int = 0
) : ProgressBar(context, attributeSet, defStyle) {
    private val divides: Int
    private val glListener = ViewTreeObserver.OnGlobalLayoutListener {
        onViewSizeDetermined(width, height)
    }
    /**
     * задание параметров
     */
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.appWhite)
        strokeWidth = AndroidUtils.dpToPx(1f, context)
    }

    private var divideStep = 0f
    /**
     * инициализация
     */
    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ProgressBarDivided, 0, 0)
                .apply { divides = getInt(R.styleable.ProgressBarDivided_divides, 0) }
        viewTreeObserver.addOnGlobalLayoutListener(glListener)
    }
    /**
     * рисование
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until divides)
            canvas.drawLine(i * divideStep, marginTop.toFloat(), i * divideStep, height - marginBottom.toFloat(), paint)
    }

    private fun onViewSizeDetermined(width: Int, height: Int) {
        viewTreeObserver.removeOnGlobalLayoutListener(glListener)
        divideStep = width.toFloat() / divides
    }
}