package ru.digipeople.qrscanner

import ru.digipeople.qrscanner.ui.activity.scanner.ScannerScreenComponent

/**
 * Компонент модуля сканнер
 *
 * @author Sostavkin Grisha
 */
interface ScannerComponent {

    fun scannerScreenComponent(): ScannerScreenComponent

    companion object {
        private lateinit var instance: ScannerComponent
        /**
         * получение сканнера
         */
        fun get(): ScannerComponent = instance
        /**
         * установка сканнера
         */
        fun set(scannerComponent: ScannerComponent) {
            instance = scannerComponent
        }
    }
}