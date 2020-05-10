package ru.digipeople.telephonebook.ui.activity.telephone

import dagger.Subcomponent
import ru.digipeople.telephonebook.ui.activity.telephone.TelephoneBookComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры телефонный справочник
 *
 * @author Sostavkin Grisha
 */
@ScreenScope
@Subcomponent
interface TelephoneBookScreenComponent {
    fun component(activityModule: ActivityModule): TelephoneBookComponent
}