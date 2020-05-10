package ru.digipeople.locotech.core.ui.base

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Класс для работы с LiveData в приложении
 *
 * @author Aleksandr Brazhkin
 */
class SingleEventLiveData<T> : MutableLiveData<T>() {

    private val observers = HashMap<Observer<in T>, ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers[observer] = wrapper
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        val wrapper = observers.remove(observer)
        if (wrapper != null) {
            super.removeObserver(wrapper)
        }
    }

    @MainThread
    override fun removeObservers(owner: LifecycleOwner) {
        observers.clear()
        super.removeObservers(owner)
    }

    @MainThread
    override fun setValue(value: T?) {
        observers.values.forEach { it.pending = true }
        super.setValue(value)
    }

    private class ObserverWrapper<T>(private val observer: Observer<T>) : Observer<T> {
        var pending = false

        override fun onChanged(t: T) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }
    }
}