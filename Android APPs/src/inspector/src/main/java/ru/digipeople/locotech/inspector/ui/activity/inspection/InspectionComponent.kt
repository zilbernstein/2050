package ru.digipeople.locotech.inspector.ui.activity.inspection

import dagger.Subcomponent
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.CyclicWorksComponent
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.RzdRemarksComponent
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.OtkRemarksComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент инспекционного контроля
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface InspectionComponent {
    fun inject(activity: InspectionActivity)
    fun presenter(): InspectionPresenter
    fun cyclicWorkComponent(): CyclicWorksComponent
    fun remarkOtkComponent(): OtkRemarksComponent
    fun remarkRzdComponent(): RzdRemarksComponent
}