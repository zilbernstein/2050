package ru.digipeople.locotech.master.ui.fragment.maindrawer

import dagger.Component
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.ui.dagger.FragmentScope
/**
 * Компонент бокового меню
 *
 * @author Aleksandr Brazhkin
 */
@FragmentScope
@Component(
        dependencies = [
            AppComponent::class
        ]
)
interface MainDrawerComponent {
    fun inject(mainDrawerFragment: MainDrawerFragment)
}