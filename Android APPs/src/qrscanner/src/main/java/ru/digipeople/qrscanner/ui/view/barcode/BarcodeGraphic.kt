package ru.digipeople.qrscanner.ui.view.barcode

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.google.android.gms.vision.barcode.Barcode
import ru.digipeople.qrscanner.ui.view.camera.GraphicOverlay

/**
 * * Класс для отрисовки рамки вокруг распознанного QR кода
 *
 * @author Sostavkin Grisha
 */
class BarcodeGraphic(overlay: GraphicOverlay) : GraphicOverlay.Graphic(overlay) {
    private val rectPaint: Paint
    private val color = Color.RED

    @Volatile
    private lateinit var barcode: Barcode

    init {
        rectPaint = Paint()
        rectPaint.color = color
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4.0f
    }

    /**
     * Метод заново отрисовывает QR код
     * @param item - переданный QR код
     */
    fun updateItem(item: Barcode) {
        barcode = item
        postInvalidate()
    }

    /**
     * Метод отрисовывает прямоугольную рамку вокруг распознанного QR кода
     */
    override fun draw(canvas: Canvas?) {

        var rect = RectF(barcode.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        canvas?.drawRect(rect, rectPaint)
    }
}