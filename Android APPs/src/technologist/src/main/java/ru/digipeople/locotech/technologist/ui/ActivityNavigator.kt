package ru.digipeople.locotech.technologist.ui

import android.app.Activity
import android.content.Intent
import ru.digipeople.locotech.technologist.ui.activity.AppNavigator
import ru.digipeople.locotech.technologist.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.technologist.ui.activity.comment.CommentParams
import ru.digipeople.locotech.technologist.ui.activity.remarks.RemarksActivity
import ru.digipeople.locotech.technologist.ui.activity.splash.SplashActivity
import ru.digipeople.locotech.technologist.ui.activity.work.WorkActivity
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.photo.ui.activity.photogallery.PhotoGalleryActivity
import ru.digipeople.telephonebook.ui.TelephoneBookNavigator
import ru.digipeople.ui.activity.base.BaseActivityNavigator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Активити навигатор
 *
 * @author Sostavkin Grisha
 */
@Singleton
class ActivityNavigator @Inject constructor(private val messageNavigator: MessageActivityNavigator,
                                            private val telephoneBookNavigator: TelephoneBookNavigator,
                                            private val appNavigator: AppNavigator) : BaseActivityNavigator() {
    /**
     * Переход на экран авторизации
     */
    fun navigateToAuthActivity(activity: Activity) {
        appNavigator.navigateToAuth()
    }
    /**
     * Переход на экран замечаний
     */
    fun navigateToRemarkActivity(activity: Activity) {
        val intent = RemarksActivity.getCallingIntent(activity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Переход на экран телефонная книга
     */
    fun navigateToTelephoneBookActivity(activity: Activity) {
        telephoneBookNavigator.navigateToTelephoneBookActivity(activity, TelephoneBookNavigator.LANDSCAPE)
    }
    /**
     * Переход на экран сообщений
     */
    fun navigateToMessageActivity(activity: Activity) {
        messageNavigator.navigateToMessageActivity(activity)
    }
    /**
     * Переход на экран работы
     */
    fun navigateToWorkActivity(activity: Activity, workId: String) {
        val intent = WorkActivity.getCallingIntent(activity, workId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Переход на экран комментария
     */
    fun navigateToCommentActivity(activity: Activity, commentParams: CommentParams) {
        val intent = CommentActivity.getCallingIntent(activity, commentParams)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Переход на экран фотогалереи
     */
    fun navigateToPhotoGalleryActivity(activity: Activity, remarkId: String) {
        val intent = PhotoGalleryActivity.getCallingIntentForRemarkInViewMode(activity, remarkId)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * Переход на экран заставки
     */
    fun navigateToSplashActivity(activity: Activity) {
        val intent = SplashActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
}