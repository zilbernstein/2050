package ru.digipeople.locotech.worker.helper

import ru.digipeople.locotech.worker.model.Client
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Провайдер текущего пользователя
 *
 * @author Kashonkov Nikita
 */
@Singleton
class ClientProvider @Inject constructor() {
    lateinit var client: Client
}