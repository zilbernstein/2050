package ru.digipeople.qrscanner.ui.activity.scanner

import ru.digipeople.qrscanner.ui.activity.QrScannerNavigator
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер сканер
 *
 * @author Sostavkin Grisha
 */
class ScannerPresenter @Inject constructor(scannerViewState: ScannerViewState,
                                           val navigator: QrScannerNavigator)
    : BaseMvpViewStatePresenter<ScannerView, ScannerViewState>(scannerViewState) {
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}
    /**
     * Обнаружен баркода
     */
    fun barcodeDetected(goodId: String) {
        view.setScanningResult(goodId)
    }
}