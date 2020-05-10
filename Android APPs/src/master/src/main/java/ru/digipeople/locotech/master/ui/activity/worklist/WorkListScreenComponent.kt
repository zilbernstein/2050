package ru.digipeople.locotech.master.ui.activity.worklist

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент добавления замечания / работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface WorkListScreenComponent {

    fun componentBuilder(): WorkListComponent.Builder
}