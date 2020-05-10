package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks

import dagger.Subcomponent
import ru.digipeople.ui.dagger.FragmentScope

/**
 * Интерфейс замечаний ОТК
 *
 * @author Kashonkov Nikita
 */
@FragmentScope
@Subcomponent
interface OtkRemarksComponent {
    fun inject(fragment: OtkRemarksFragment)
    fun presenter(): OtkRemarksPresenter
}