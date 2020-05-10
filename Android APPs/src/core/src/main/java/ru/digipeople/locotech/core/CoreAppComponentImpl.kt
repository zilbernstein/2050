package ru.digipeople.locotech.core

import dagger.Component
import ru.digipeople.thingworx.ThingWorxComponent
import javax.inject.Singleton

/**
 * Реализация [CoreAppComponent]
 */
@Singleton
@Component(
        modules = [
            CoreAppModule::class
        ],
        dependencies = [
            ThingWorxComponent::class
        ])
interface CoreAppComponentImpl : CoreAppComponent