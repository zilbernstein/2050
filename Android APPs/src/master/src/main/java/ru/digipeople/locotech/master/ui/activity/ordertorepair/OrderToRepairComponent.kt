package ru.digipeople.locotech.master.ui.activity.ordertorepair

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.dagger.ActivityScope
/**
 * Компонент запрос на получение ТМЦ для ремонта
 */
@ActivityScope
@Component(
        modules = [
            OrderToRepairModule::class,
            ActivityModule::class
        ],
        dependencies = [
            AppComponent::class,
            CoreAppComponent::class,
            UiComponent::class
        ])
interface OrderToRepairComponent {
    fun inject(orderToRepairActivity: OrderToRepairActivity)

    fun viewModelFactory(): ViewModelProvider.Factory
}