package ru.hoff.tablet.ui.fragment.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : Fragment> T.withArgs(receiver: Bundle.() -> Unit): T {
    arguments = Bundle().apply(receiver)
    return this
}

@Suppress("unused")
inline fun <reified T> Fragment.arg(key: String): ArgDelegate<T> {
    return ArgDelegate {
        it.arguments?.get(key) as T
    }
}

class ArgDelegate<T>(private val extractFunc: (Fragment) -> T) : ReadOnlyProperty<Fragment, T> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return extractFunc(thisRef)
    }
}