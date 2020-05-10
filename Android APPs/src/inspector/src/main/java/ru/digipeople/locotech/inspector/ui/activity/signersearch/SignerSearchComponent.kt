package ru.digipeople.locotech.inspector.ui.activity.signersearch

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент поиска подписантов
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface SignerSearchComponent {
    fun inject(searchPerformerActivity: SignerSearchActivity)

    fun presenter(): SignerSearchPresenter
}