package ru.digipeople.locotech.inspector.ui.activity.checklist

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент чек-листа
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface CheckListScreenComponent {
    fun component(module: ActivityModule): CheckListComponent
}