package ru.digipeople.message

import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton

/**
 * Реализация интерфейса [MessagesComponent]
 */
@Singleton
@Component(
        dependencies = [
            UiComponent::class,
            CoreAppComponent::class,
            ThingWorxComponent::class
        ],
        modules = [
            MessagesModule::class
        ]
)
interface MessagesComponentImpl : MessagesComponent