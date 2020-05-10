package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks

import dagger.Subcomponent
import ru.digipeople.ui.dagger.FragmentScope

/**
 * Компонент цикловых работ
 * @author Kashonkov Nikita
 */
@FragmentScope
@Subcomponent
interface CyclicWorksComponent {
    fun inject(fragment: CyclicWorksFragment)
    fun presenter(): CyclicWorksPresenter
}