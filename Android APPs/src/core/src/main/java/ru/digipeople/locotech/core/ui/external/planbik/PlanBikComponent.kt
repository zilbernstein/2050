package ru.digipeople.locotech.core.ui.external.planbik

import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Компонент для Плана БИК
 *
 * @author Aleksandr Brazhkin
 */
@ActivityScope
@Component(
        dependencies = [
            CoreAppComponent::class
        ]
)
interface PlanBikComponent {
    fun inject(externalPlanBik: ExternalPlanBik)
}