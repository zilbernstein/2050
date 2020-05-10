package ru.digipeople.message.ui.activity.chooseaddressee

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент экрана структуры выбора адресата
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface ChooseAddresseeScreenComponent {
    fun component(activityModule: ActivityModule): ChooseAddresseComponent
}