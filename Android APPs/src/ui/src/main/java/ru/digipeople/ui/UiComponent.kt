package ru.digipeople.ui

import ru.digipeople.ui.activity.base.BaseAuthorizationChecker
import ru.digipeople.ui.activity.base.MainDrawerDelegateFactory
import ru.digipeople.ui.mvp.core.MvpProcessor

/**
 * Интерфейс UI модуля
 *
 * @author Sostavkin Grisha
 */
interface UiComponent {
    /**
     * Класс, управляющий презентерами
     */
    fun mvpProcessor(): MvpProcessor

    fun baseAuthorizationChecker(): BaseAuthorizationChecker

    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactory

    companion object {
        private lateinit var instance: UiComponent

        fun get(): UiComponent = instance

        fun set(component: UiComponent) {
            instance = component
        }
    }
}