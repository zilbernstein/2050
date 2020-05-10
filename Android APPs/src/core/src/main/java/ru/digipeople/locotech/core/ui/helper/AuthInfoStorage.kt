package ru.digipeople.locotech.core.ui.helper

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для хранения информации для авторизации
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class AuthInfoStorage @Inject constructor() {
    var loginPassPair = Pair("", "")
}