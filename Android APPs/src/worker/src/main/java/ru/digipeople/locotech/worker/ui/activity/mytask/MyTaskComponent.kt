package ru.digipeople.locotech.worker.ui.activity.mytask

import dagger.Subcomponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент структуры работы
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface MyTaskComponent {

    fun inject(myTaskActivity: MyTaskActivity)

    fun presenter(): MyTaskPresenter
}