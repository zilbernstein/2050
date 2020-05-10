package ru.digipeople.locotech.worker.ui.activity

import android.app.Activity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Расширения для активити
 */

@Suppress("unused")
inline fun <reified T> Activity.extra(key: String, default: T): ExtraDelegate<T> {
    return ExtraDelegate {
        it.intent?.extras?.get(key) as T? ?: default
    }
}

class ExtraDelegate<T>(private val extractFunc: (Activity) -> T) : ReadOnlyProperty<Activity, T> {
    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return extractFunc(thisRef)
    }
}