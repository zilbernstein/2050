package ru.digipeople.locotech.inspector.ui.activity.summary

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Компонент суммарной информации
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface SummaryScreenComponent {
    fun component(module: ActivityModule): SummaryComponent
}