package ru.digipeople.qrscanner.ui.activity.scanner

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс структуры сканнера
 *
 * @author Sostavkin Grisha
 */
interface ScannerView: MvpView {
    /**
     * установить результат сканирования
     */
    fun setScanningResult(id: String)

}