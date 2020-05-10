package ru.digipeople.qrscanner.ui.view.camera

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource

/**
 * * View для отображения изображения с камеры с наложенной на него информации о QR кодах
 *
 * @author Sostavkin Grisha
 */
class CameraSourcePreview : ViewGroup {
    private var surfaceView: SurfaceView
    private var cameraSource: CameraSource? = null
    var startRequested: Boolean = false
    var surfaceAvailable: Boolean = false

    private lateinit var graphicOverlay: GraphicOverlay
    /**
     * Конструктор
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        surfaceView = SurfaceView(context)
        surfaceView.holder.addCallback(SurfaceCallback())
        addView(surfaceView)
    }
    /**
     * Действия при старте
     */
    fun start(cameraSource: CameraSource?) {
        if (cameraSource == null) {
            stop()
        }

        this.cameraSource = cameraSource!!
        startRequested = true
        startIfReady()
    }
    /**
     * Действия при старте
     */
    fun start(cameraSource: CameraSource, overlay: GraphicOverlay) {
        graphicOverlay = overlay
        start(cameraSource)
    }

    fun stop() {
        if (cameraSource != null)
            cameraSource?.stop()
    }

    fun release() {
        if (cameraSource != null) {
            cameraSource?.release()
            cameraSource = null
        }
    }

    private fun startIfReady() {
        if (startRequested && surfaceAvailable) {
            cameraSource?.start(surfaceView.holder)
            /**
             * установка размеров
             */
            val size = cameraSource!!.previewSize
            val min = Math.min(size.width, size.height)
            val max = Math.max(size.width, size.height)

            graphicOverlay.setCameraInfo(min, max, cameraSource!!.cameraFacing)
            graphicOverlay.clear()
            startRequested = false
        }
    }
    /**
     * Определение размеров лэйаута
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val layoutWidth = right - left
        val layoutHeight = bottom - top

        for (i in 0 until childCount) {
            getChildAt(i).layout(0, 0, layoutWidth, layoutHeight)
        }

        startIfReady()

    }

    inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            surfaceAvailable = false
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            surfaceAvailable = true
            startIfReady()
        }
    }

}