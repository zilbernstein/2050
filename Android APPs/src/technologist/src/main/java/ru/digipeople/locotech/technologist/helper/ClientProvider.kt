package ru.digipeople.locotech.technologist.helper

import ru.digipeople.locotech.technologist.model.Client
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Провайдер текущего пользователя
 *
 * @author Sostavkin Grisha
 */
@Singleton
class ClientProvider @Inject constructor() {
    lateinit var client: Client
}