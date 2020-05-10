package ru.digipeople.ui.activity.base

import androidx.appcompat.app.AppCompatActivity

/**
 * Првоеряет состояние авторизации(того что [ThingWorxClient] != null)
 *
 * @author Kashonkov Nikita
 */
interface BaseAuthorizationChecker {
    fun checkAuthorization(activity: AppCompatActivity)
}