package ru.digipeople.qrscanner.tracker

import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import ru.digipeople.qrscanner.ui.view.barcode.BarcodeGraphic
import ru.digipeople.qrscanner.ui.view.camera.GraphicOverlay

/**
 * * Трекер для QR кодов
 * Генерируется для каждого нового затрекеного QR кода
 *
 * @author Sostavkin Grisha
 */
class BarcodeTracker  constructor(val overlay: GraphicOverlay, val graphic: BarcodeGraphic, val listener: BarcodeTrackerListener) : Tracker<Barcode>() {

    override fun onMissing(p0: Detector.Detections<Barcode>?) {
        overlay.remove(graphic)
    }
    /**
     * Действия при  обновлнии
     */
    override fun onUpdate(p0: Detector.Detections<Barcode>?, p1: Barcode) {
        overlay.add(graphic)
        graphic.updateItem(p1)
        // Если на экране присутствует единственный распознанный QR код и закодированный текст не пустой, передаем слушаетлю событие успешной детекции QR'a
        if (p0!!.detectedItems.size() == 1) {
            if (p1.rawValue != null && !p1.rawValue.isEmpty()) {
                listener.barcodeDetected(p1.rawValue)
            }
        }
    }
    /**
     * Действия при завершении работы
     */
    override fun onDone() {
        overlay.remove(graphic)
    }

    /**
     * Слушатель трекера
     */
    interface BarcodeTrackerListener {
        fun barcodeDetected(text: String)
    }
}