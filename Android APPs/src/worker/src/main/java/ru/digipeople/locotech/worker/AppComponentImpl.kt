package ru.digipeople.locotech.worker

import dagger.Component
import ru.digipeople.message.MessagesComponent
import ru.digipeople.photo.PhotoComponent
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton

/**
 * Реализация [AppComponent]
 *
 * @author Kashonkov Nikita
 */
@Singleton
@Component(modules = [AppModule::class], dependencies = [UiComponent::class, ThingWorxComponent::class, MessagesComponent::class, PhotoComponent::class])
interface AppComponentImpl: AppComponent