package ru.digipeople.locotech.master.ui.activity.remark

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Экранный компонент замечаний
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface RemarkScreenComponent {
   fun component(activityModule: ActivityModule): RemarkComponent
}