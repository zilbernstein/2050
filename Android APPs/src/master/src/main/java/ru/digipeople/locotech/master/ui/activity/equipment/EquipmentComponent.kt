package ru.digipeople.locotech.master.ui.activity.equipment

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент локомотивов на учатске
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface EquipmentComponent {

    fun inject(equipmentActivity: EquipmentActivity)

    fun locomotivePresenter(): EquipmentPresenter
}