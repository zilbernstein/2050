package ru.digipeople.locotech.master.ui.activity.status

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент статуса работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface StatusScreenComponent {

    fun statusComponent(activityModule: ActivityModule): StatusComponent
}