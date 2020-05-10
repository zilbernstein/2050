package ru.digipeople.locotech.worker.ui.fragment.maindrawer

import dagger.Component
import ru.digipeople.ui.dagger.FragmentScope
import ru.digipeople.locotech.worker.AppComponent

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