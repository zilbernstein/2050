package ru.digipeople.locotech.core.data

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SharedPreference модуля core
 */
@Singleton
class CorePrefs @Inject constructor(context: Context) {
    private val prefs = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE)
    /**
     * SharedPreference модуля core
     */
    /**
     * URL
     */
    var thingWorksUrl: String?
        get() = prefs.getString(Key.THING_WORKS_URL, null)
        set(thingWorksUrl) = prefs.edit().putString(Key.THING_WORKS_URL, thingWorksUrl).apply()
    /**
     * Имя пользователя
     */
    var userName: String?
        get() = prefs.getString(Key.USER_NAME, null)
        set(userName) = prefs.edit().putString(Key.USER_NAME, userName).apply()
    /**
     * Token
     */
    var fbcToken: String
        get() = prefs.getString(Key.FBC_TOKEN, "")!!
        set(fbcToken) = prefs.edit().putString(Key.FBC_TOKEN, fbcToken).apply()

    private object Key {
        const val THING_WORKS_URL = "THING_WORKS_URL"
        const val USER_NAME = "USER_NAME"
        const val FBC_TOKEN = "FBC_TOKEN"
    }

    companion object {
        /**
         * Наименование файла
         */
        private const val SHARED_PREFS_FILE_NAME = "CorePrefs"
    }
}