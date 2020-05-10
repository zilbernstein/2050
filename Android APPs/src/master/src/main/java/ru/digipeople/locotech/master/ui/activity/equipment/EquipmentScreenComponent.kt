package ru.digipeople.locotech.master.ui.activity.equipment

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Экранный компонент локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface EquipmentScreenComponent {

    fun locomotiveComponent(activityModule: ActivityModule): EquipmentComponent
}