package ru.digipeople.ui.activity.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import ru.digipeople.ui.dagger.ActivityScope

/**
 * Активити модуль
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
@Module
class ActivityModule constructor(val activity: AppCompatActivity) {
    @Provides
    internal fun activity(): Activity = activity

    @Provides
    internal fun appCompatActivity(): AppCompatActivity = activity

    @Provides
    @ActivityScope
    fun mainDrawerDelegate(mainDrawerDelegateFactory: MainDrawerDelegateFactory,
                           toolbarDelegate: ToolbarDelegate): MainDrawerDelegate {
        return mainDrawerDelegateFactory.createMainDrawerDelegate(activity, toolbarDelegate)
    }
}