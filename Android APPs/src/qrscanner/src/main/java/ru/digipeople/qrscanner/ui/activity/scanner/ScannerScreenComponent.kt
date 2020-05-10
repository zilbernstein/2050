package ru.digipeople.qrscanner.ui.activity.scanner

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры сканнер
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface ScannerScreenComponent {
    fun component(activityModule: ActivityModule): ScannerComponent
}