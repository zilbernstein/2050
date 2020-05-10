package ru.digipeople.qrscanner

import dagger.Component
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton

/**
 * Реализация [ScannerComponent]
 *
 * @author Sostavkin Grisha
 */
@Singleton
@Component(modules = [ScannerModule::class], dependencies = [UiComponent::class])
interface ScannerComponentImpl: ScannerComponent