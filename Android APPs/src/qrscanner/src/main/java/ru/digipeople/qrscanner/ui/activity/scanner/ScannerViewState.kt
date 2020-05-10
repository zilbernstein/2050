package ru.digipeople.qrscanner.ui.activity.scanner

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState Сканнер
 *
 * @author Sostavkin Grisha
 */
class ScannerViewState @Inject constructor() : BaseMvpViewState<ScannerView>(), ScannerView {
    override fun onViewAttached(view: ScannerView?) {}

    override fun onViewDetached(view: ScannerView?) {}
    /**
     * Утановить результат сканирования
     */
    override fun setScanningResult(id: String) {
        forEachView {it.setScanningResult(id) }
    }
}