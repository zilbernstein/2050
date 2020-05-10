package ru.digipeople.locotech.master.ui.activity.remark

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент замечаний
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface RemarkComponent{
    fun inject(activity: RemarkActivity)

    fun presenter(): RemarkPresenter
}