package ru.digipeople.locotech.core.ui.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Абстрактная реализация [Application.ActivityLifecycleCallbacks].
 *
 * @author Aleksandr Brazhkin
 */
abstract class AbstractActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    /**
     * при создании активити
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }
    /**
     * при старте активити
     */
    override fun onActivityStarted(activity: Activity) {

    }
    /**
     * при возобновлении активити
     */
    override fun onActivityResumed(activity: Activity) {

    }
    /**
     * при приостановке активити
     */
    override fun onActivityPaused(activity: Activity) {

    }
    /**
     * При остановке активити
     */
    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }
    /**
     * При уничтожении активити
     */
    override fun onActivityDestroyed(activity: Activity) {

    }
}
