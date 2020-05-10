package ru.digipeople.message.helper

import ru.digipeople.message.model.CreatingMessage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс предостовляющий данные для создания сообщения 
 */
@Singleton
class CreatingMessageProvider @Inject constructor() {
    var creatingMessage: CreatingMessage? = null
}