package ru.digipeople.thingworx

import dagger.Component
import javax.inject.Singleton

/**
 * Реализация [ThingWorxComponent]
 *
 * @author Kashonkov Nikita
 */
@Singleton
@Component(modules = [ThingWorxModule::class])
interface ThingWorxComponentImpl : ThingWorxComponent