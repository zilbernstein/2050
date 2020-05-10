package ru.digipeople.ui.activity.base

import androidx.appcompat.app.AppCompatActivity

/**
 * Создает [MainDrawerDelegate]
 *
 * @author Aleksandr Brazhkin
 */
interface MainDrawerDelegateFactory {
    fun createMainDrawerDelegate(activity: AppCompatActivity, toolbarDelegate: ToolbarDelegate): MainDrawerDelegate
}