package ru.digipeople.utils.model

/**
 * Ошибка для отображения пользователю в UI.
 */
interface UserError {
    /**
     * Текст сообщения
     */
    val message: String

    companion object {
        val NO_ERROR: UserError = object : UserError {
            /**
             * Значение по умолчанию
             **/
            override val message = ""
        }
    }
}

class UserErrorImpl(override val message: String) : UserError
