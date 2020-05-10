package ru.digipeople.qrscanner.ui.activity

import android.app.Activity
import ru.digipeople.qrscanner.ui.activity.scanner.ScannerActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигатор для модуля Сканнер
 *
 * @author Sostavkin Grisha
 */
@Singleton
class QrScannerNavigator @Inject constructor() {
    fun navigateBack() {}
    /**
     * переход к экрану сканера
     */
    fun navigateToScannerActivity(activity: Activity, requestCode: Int) {
        val intent = ScannerActivity.getCallingIntentForResult(activity)
        activity.startActivityForResult(intent, requestCode)
    }
}