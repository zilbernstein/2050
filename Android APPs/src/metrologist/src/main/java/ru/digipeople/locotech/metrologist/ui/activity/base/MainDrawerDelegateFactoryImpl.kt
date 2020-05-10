package ru.digipeople.locotech.metrologist.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MainDrawerDelegateFactory
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Реализация интерфейса [MainDrawerDelegateFactory]
 *
 * @author Aleksandr Brazhkin
 */
class MainDrawerDelegateFactoryImpl @Inject constructor() : MainDrawerDelegateFactory {
    override fun createMainDrawerDelegate(activity: AppCompatActivity, toolbarDelegate: ToolbarDelegate): MainDrawerDelegate {
        return MainDrawerDelegateImpl(activity, toolbarDelegate)
    }
}