package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks

import dagger.Subcomponent
import ru.digipeople.ui.dagger.FragmentScope

/**
 * Компонент замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
@FragmentScope
@Subcomponent
interface RzdRemarksComponent {
    fun inject(fragment: RzdRemarksFragment)
    fun presenter(): RzdRemarksPresenter
}