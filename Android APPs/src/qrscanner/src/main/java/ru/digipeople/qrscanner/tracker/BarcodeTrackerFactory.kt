package ru.digipeople.qrscanner.tracker

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import ru.digipeople.qrscanner.ui.view.barcode.BarcodeGraphic
import ru.digipeople.qrscanner.ui.view.camera.GraphicOverlay

/**
 * Фабрика для производства экземпляров [BarcodeTracker]
 *
 * @author Sostavkin Grisha
 */
class BarcodeTrackerFactory constructor(val graphicOverlay: GraphicOverlay, val listener: BarcodeTracker.BarcodeTrackerListener): MultiProcessor.Factory<Barcode> {

    override fun create(p0: Barcode?): Tracker<Barcode> {
        val graphic = BarcodeGraphic(graphicOverlay)
        return BarcodeTracker(graphicOverlay, graphic, listener)
    }
}