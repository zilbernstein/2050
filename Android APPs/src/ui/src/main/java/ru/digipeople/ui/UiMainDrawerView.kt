package ru.digipeople.ui

import ru.digipeople.ui.activity.base.MainDrawerDelegate

/**
 * Интерфейс для работы с боковым меню
 */
interface UiMainDrawerView {
    /**
     * Инициализация
     */
    fun init(mainDrawerDelegate: MainDrawerDelegate)
    /**
     * Дествия при открытии меню
     */
    fun onDrawerOpened()
    /**
     * переход назад
     */
    fun navigateBack()
}