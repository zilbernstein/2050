package ru.digipeople.locotech.inspector.ui.activity.remarksearch

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface RemarkSearchComponent {
    fun inject(remarkSearchActivity: RemarkSearchActivity)

    fun presenter(): RemarkSearchPresenter
}