package ru.digipeople.qrscanner.ui.view.camera

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource

/**
 * View для отображения информации о всех наблюдаемых QR кодах
 * @author Sostavkin Grisha
 */
class GraphicOverlay (context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val lock = Any()
    private var previewWidth = 0
    private var widthScaleFactor = 1.0f
    private var previewHeight = 0
    private var heightScaleFactor = 1.0f
    private var facingCamera = CameraSource.CAMERA_FACING_BACK

    /**
     * Сэт гафических объектов, представленных на слое
     */
    private val graphics = HashSet<Graphic>()

    /**
     * Добавляем элемент
     */
    fun add(graphic: Graphic) {
        synchronized(lock) {
            graphics.add(graphic)
        }
        postInvalidate()
    }
    /**
     * Удаляем все изображения со слоя
     */
    fun clear() {
        synchronized(lock) {
            graphics.clear()
        }
        postInvalidate()
    }

    /**
     * Удаляем конкретное изображение со слоя
     */
    fun remove(graphic: Graphic) {
        synchronized(lock) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }

    /**
     * Устанавливает информацию о камере, и первоначальных размерах
     */
    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facingCamera: Int) {
        synchronized(lock) {
            this.previewWidth = previewWidth
            this.previewHeight = previewHeight
            this.facingCamera = facingCamera
        }
        postInvalidate()
    }

    /**
     * Отрисовываем слой со всем графическими объектами из [graphics]
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        synchronized(lock) {
            if ((previewHeight != 0) && (previewHeight != 0)) {
                widthScaleFactor = canvas?.width!!.toFloat() / previewWidth.toFloat()
                heightScaleFactor = canvas.height.toFloat() / previewHeight.toFloat()
            }

            for (graphic in graphics) {
                graphic.draw(canvas)
            }
        }
    }

    /**
     * Базовый класс для графических объектов(QR или штрих кодов), отображаемыx на текущем слое GraphicOverlay
     */
    abstract class Graphic constructor(val overlay: GraphicOverlay) {
        abstract fun draw(canvas: Canvas?)

        fun scaleX(horizontal: Float): Float {
            return horizontal * overlay.widthScaleFactor
        }

        fun scaleY(vertical: Float): Float {
            return vertical * overlay.heightScaleFactor
        }

        fun translateX(x: Float): Float {
            return if (overlay.facingCamera == CameraSource.CAMERA_FACING_FRONT) {
                overlay.width - scaleX(x)
            } else {
                scaleX(x)
            }
        }

        fun translateY(y: Float): Float {
            return scaleY(y)
        }

        fun postInvalidate() {
            overlay.postInvalidate()
        }
    }
}