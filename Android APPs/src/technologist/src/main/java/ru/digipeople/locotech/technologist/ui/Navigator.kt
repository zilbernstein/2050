package ru.digipeople.locotech.technologist.ui

import android.app.Activity
import ru.digipeople.locotech.technologist.ui.activity.comment.CommentParams
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Навигатор МП Технолог
 *
 * @author Sostavkin Grisha
 */
@Singleton
class Navigator @Inject constructor(private val activityNavigator: ActivityNavigator) {

    private var activity: Activity? = null

    internal fun onResume(activity: Activity) {
        this.activity = activity
    }

    internal fun onPause() {
        this.activity = null
    }
    /**
     * Переход на экран авторизации
     */
    fun navigateToAuthActivity() {
        if (activity != null) {
            activityNavigator.navigateToAuthActivity(activity!!)
        }
    }
    /**
     * Переход на экран замечаний
     */
    fun navigateToRemarkActivity() {
        if (activity != null) {
            activityNavigator.navigateToRemarkActivity(activity!!)
        }
    }
    /**
     * Переход на экран телефонной книги
     */
    fun navigateToTelephoneBookActivity() {
        if (activity != null) {
            activityNavigator.navigateToTelephoneBookActivity(activity!!)
        }
    }
    /**
     * Переход на экран сообщений
     */
    fun navigateToMessageActivity() {
        if (activity != null) {
            activityNavigator.navigateToMessageActivity(activity!!)
        }
    }
    /**
     * Переход на экран работы
     */
    fun navigateToWorkActivity(workId: String) {
        if (activity != null) {
            activityNavigator.navigateToWorkActivity(activity!!, workId)
        }
    }
    /**
     * Переход на экран комментариев
     */
    fun navigateToCommentActivity(commentParams: CommentParams) {
        if (activity != null) {
            activityNavigator.navigateToCommentActivity(activity!!, commentParams)
        }
    }
    /**
     * Переход на экран фотогалереи
     */
    fun navigateToPhotoGalleryActivity(remarkId: String) {
        if (activity != null) {
            activityNavigator.navigateToPhotoGalleryActivity(activity!!, remarkId)
        }
    }
    /**
     * Переход на экран загрузки
     */
    fun navigateToSplashActivity() {
        if (activity != null) {
            activityNavigator.navigateToSplashActivity(activity!!)
        }
    }
    /**
     * Переход на предыдущий экран
     */
    fun navigateBack() {
        if (activity != null) {
            activityNavigator.navigateBack(activity!!)
        }
    }
}