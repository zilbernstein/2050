package ru.digipeople.telephonebook.ui.activity.telephonedirectory

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры телефонный справочник
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface TelephoneDirectoryScreenComponent {
    fun component(activityModule: ActivityModule): TelephoneDirectoryComponent
}