package ru.digipeople.locotech.core.push.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.api.ThingWorxUrlProvider
import ru.digipeople.locotech.core.helper.NotificationHelper
import ru.digipeople.logger.LoggerFactory
import javax.inject.Inject

/**
 * Firebase service для пушей
 */
class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val logger = LoggerFactory.getLogger(AppFirebaseMessagingService::class)

    //region Di
    @Inject
    lateinit var fbcTokenManager: FbcTokenManager
    @Inject
    lateinit var notificationHelper: NotificationHelper
    @Inject
    lateinit var thingWorxUrlProvider: ThingWorxUrlProvider
    //endregion

    override fun onCreate() {
        super.onCreate()
        CoreAppComponent.get().inject(this)
    }
    /**
     * Действия при получении сообщения
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        /**
         * логирование
         */
        logger.trace("From: ${remoteMessage.from}")
        logger.trace("Data: " + remoteMessage.data)
        logger.trace("Notification: " + remoteMessage.notification)

        val title = remoteMessage.data["title"] ?: ""
        val body = remoteMessage.data["body"] ?: ""
        val url = remoteMessage.data["url"] ?: ""

        if (url == thingWorxUrlProvider.current) {
            /**
             * отображение уведомления
             */
            notificationHelper.showNotification(title, body)
        }
    }

    override fun onNewToken(newtoken: String) {
        fbcTokenManager.updateFbcTokenAsync()
    }
}