package ru.digipeople.locotech.master.ui.activity.remarksearch

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент добавления / создания замечания
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface RemarkSearchComponent {
    fun inject(remarkSearchActivity: RemarkSearchActivity)

    fun presenter(): RemarkSearchPresenter
}