package ru.digipeople.locotech.core.ui.activity.auth

import android.app.Activity
import ru.digipeople.locotech.core.ui.activity.auth.interactor.AuthWorkerProxy

/**
 * Параметры для авторизации
 *
 * @author Nikita Sychev
 **/
object AuthParams {
    /**
     * Auth credentials for debugging purposes
     * @sample Pair("Username", "password")
     */
    var debugCredentials: Pair<String, String>? = null
    var specializationTitleText = ""

    var authWorkerProxy: AuthWorkerProxy? = null
    var onSuccess: ((Activity) -> Unit)? = null
}