package ru.digipeople.locotech.inspector.ui.helper

import ru.digipeople.locotech.inspector.model.Client
import ru.digipeople.locotech.inspector.model.UserRole
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс хранилище данных пользователя после авторизации
 * @author Kashonkov Nikita
 */
@Singleton
class ClientProvider @Inject constructor() {
    val userRole
        get() = client.role

    var client: Client = Client()
        set(value) {
            field = value
            onClientChangedListeners.forEach { it() }
        }

    private val onClientChangedListeners = mutableSetOf<(() -> Unit)>()

    fun addOnClientChangedListener(listener: () -> Unit) {
        onClientChangedListeners.add(listener)
    }

    fun removeOnClientChangedListener(listener: () -> Unit) {
        onClientChangedListeners.remove(listener)
    }

    fun isSldUser() = isUserRole(UserRole.SLD)

    fun isRzdUser() = isUserRole(UserRole.RZD)

    private fun isUserRole(userRole: UserRole) = client.role == userRole
}
