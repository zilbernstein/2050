package ru.digipeople.message.ui.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import ru.digipeople.locotech.core.ScreenOrientation
import ru.digipeople.message.ui.activity.chooseaddressee.ChooseAddresseActivity
import ru.digipeople.message.ui.activity.message.MessageActivity
import ru.digipeople.message.ui.activity.messagedetail.MessageDetailActivity
import ru.digipeople.message.ui.activity.messagelist.MessageListActivity
import ru.digipeople.message.ui.activity.writemessage.WriteMessageActivity
import ru.digipeople.ui.activity.base.BaseActivityNavigator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Активити навигатор для модуля message
 */
@Singleton
class MessageActivityNavigator @Inject constructor(@ScreenOrientation private val screenOrientation: Int) : BaseActivityNavigator() {
    /**
     * переход к сообщениям
     */
    fun navigateToMessageActivity(activity: Activity) {

        val intent = if (screenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            MessageActivity.getCallingIntent(activity)
        } else {
            MessageListActivity.getCallingIntent(activity)
        }

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к созданию нового сообщения
     */
    fun navigateToNewMessageActivity(activity: Activity) {
        val intent = WriteMessageActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к деталке сообщения
     */
    fun navigateToMessageDetailActivity(activity: Activity) {
        val intent = MessageDetailActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к выбору адресатов
     */
    fun navigateToChooseAddresseActivity(activity: Activity) {
        val intent = ChooseAddresseActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к написанию сообщения
     */
    fun navigateToWriteMessageActivity(activity: Activity) {
        val intent = WriteMessageActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
    /**
     * переход к пересылке сообщения
     */
    fun navigateToResendMessageActivity(activity: Activity) {
        val intent = WriteMessageActivity.getCallingIntent(activity)
        activity.startActivity(intent)
        animForward(activity)
    }
}