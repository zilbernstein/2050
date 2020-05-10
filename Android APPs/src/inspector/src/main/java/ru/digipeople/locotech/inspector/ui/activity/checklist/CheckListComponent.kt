package ru.digipeople.locotech.inspector.ui.activity.checklist

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент чек-листа
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface CheckListComponent {
    fun presenter(): CheckListPresenter
    fun inject(activity: CheckListActivity)
}