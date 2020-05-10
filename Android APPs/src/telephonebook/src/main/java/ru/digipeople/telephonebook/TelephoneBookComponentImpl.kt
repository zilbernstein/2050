package ru.digipeople.telephonebook

import dagger.Component
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton

/**
 * Реализация [TelephoneBookComponent]
 *
 * @author Sostavkin Grisha
 */
@Singleton
@Component(modules = [TelephoneBookModule::class], dependencies = [UiComponent::class, ThingWorxComponent::class])
interface TelephoneBookComponentImpl : TelephoneBookComponent