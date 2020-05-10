package ru.digipeople.ui

import dagger.Component
import javax.inject.Singleton

/**
 * Реализация [UiComponent]
 *
 * @author Sostavkin Grisha
 */
@Singleton
@Component(modules = [UiModule::class])
interface UiComponentImpl: UiComponent