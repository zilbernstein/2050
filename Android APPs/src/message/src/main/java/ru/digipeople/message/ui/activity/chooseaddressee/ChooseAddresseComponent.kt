package ru.digipeople.message.ui.activity.chooseaddressee

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент структуры выбора адресата
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ChooseAddresseComponent {
    fun inject(activity: ChooseAddresseActivity)

    fun presenter(): ChooseAddresseePresenter
}