package ru.digipeople.utils

import android.util.Base64

/**
 * Объект для работы с хедером при авторизации
 *
 * @author Aleksandr Brazhkin
 */
object HttpUtils {
    fun buildBasicAuthHeader(username: String, password: String): String {
        val authEncoded = Base64.encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP)
        return "Basic $authEncoded";
    }
}