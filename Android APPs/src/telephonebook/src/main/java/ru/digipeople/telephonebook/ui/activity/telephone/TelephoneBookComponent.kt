package ru.digipeople.telephonebook.ui.activity.telephone

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры телефонный справочник
 *
 * @author Sostavkin Grisha
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface TelephoneBookComponent {
    fun inject(telephoneBookActivity: TelephoneBookActivity)

    fun telephoneBookPresenter(): TelephoneBookPresenter
}