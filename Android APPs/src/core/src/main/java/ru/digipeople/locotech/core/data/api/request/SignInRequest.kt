package ru.digipeople.locotech.core.data.api.request

import com.google.gson.annotations.SerializedName

/**
 * Запрос токена
 */
class SignInRequest(
        /**
         * FCM token
         */
        @SerializedName("token")
        var fcmToken: String = ""
)