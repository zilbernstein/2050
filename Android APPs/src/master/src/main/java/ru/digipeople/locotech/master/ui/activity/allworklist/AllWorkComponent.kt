package ru.digipeople.locotech.master.ui.activity.allworklist

import dagger.BindsInstance
import dagger.Subcomponent
import ru.digipeople.ui.dagger.ActivityScope
import ru.digipeople.ui.activity.base.ActivityModule

/**
 * Компонент списка работ
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface AllWorkComponent {

    fun inject(allWorkActivity: AllWorkActivity)

    fun presenter(): AllWorkPresenter


    @Subcomponent.Builder
    interface Builder {
        fun activityModule(activityModule: ActivityModule): Builder
        /**
         * Идентификатор замечания
         */
        @BindsInstance
        fun remarkId(remarkId: String): Builder
        /**
         * Идентификатор точки вызова
         */
        @BindsInstance
        fun callingId(callingId: Int): Builder

        fun build(): AllWorkComponent
    }
}