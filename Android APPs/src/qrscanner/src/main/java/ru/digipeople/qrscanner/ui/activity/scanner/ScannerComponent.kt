package ru.digipeople.qrscanner.ui.activity.scanner

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры сканнер
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ScannerComponent {
    fun presenter(): ScannerPresenter
    fun inject(scannerActivity: ScannerActivity)
}