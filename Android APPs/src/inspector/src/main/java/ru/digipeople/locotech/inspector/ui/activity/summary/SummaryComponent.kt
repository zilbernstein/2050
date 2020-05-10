package ru.digipeople.locotech.inspector.ui.activity.summary

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент суммарной информации
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface SummaryComponent {
    fun presenter(): SummaryPresenter
    fun inject(activity: SummaryActivity)
}