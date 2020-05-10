package ru.digipeople.locotech.inspector.ui.activity.remarksearch

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Экранный компонент добавления/выбора замечаний
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface RemarkSearchScreenComponent {
    fun  component(activiyModule: ActivityModule): RemarkSearchComponent
}