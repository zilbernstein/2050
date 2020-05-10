package ru.digipeople.locotech.inspector.ui.activity.print

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент печати
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface PrintComponent {
    fun presenter(): PrintPresenter
    fun inject(activity: PrintActivity)
}