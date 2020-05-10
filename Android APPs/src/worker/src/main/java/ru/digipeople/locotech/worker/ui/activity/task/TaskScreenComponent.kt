package ru.digipeople.locotech.worker.ui.activity.task

import dagger.Subcomponent
import ru.digipeople.ui.dagger.ScreenScope

/**
 * Экранный компонент структуры задания
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
@Subcomponent
interface TaskScreenComponent {

    fun componentBuilder(): TaskComponent.Builder
}