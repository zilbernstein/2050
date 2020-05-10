package ru.digipeople.photo

import dagger.Component
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton

/**
 * Реализация [PhotoComponent]
 *
 * @author Kashonkov Nikita
 */
@Singleton
@Component(dependencies = [UiComponent::class, ThingWorxComponent::class], modules = [PhotoModule::class])
interface PhotoComponentImpl: PhotoComponent {
}