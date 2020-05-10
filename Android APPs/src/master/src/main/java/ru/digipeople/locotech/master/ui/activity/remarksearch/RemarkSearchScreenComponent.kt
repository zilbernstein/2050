package ru.digipeople.locotech.master.ui.activity.remarksearch

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент добавления / создания замечания
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface RemarkSearchScreenComponent {
    fun  component(activiyModule: ActivityModule): RemarkSearchComponent
}