package ru.digipeople.telephonebook.ui.activity.telephonedirectory

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры telephonedirectory
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TelephoneDirectoryComponent {
    fun inject(telephoneDirectoryActivity: TelephoneDirectoryActivity)

    fun telephoneDirectoryPresenter(): TelephoneDirectoryPresenter
}