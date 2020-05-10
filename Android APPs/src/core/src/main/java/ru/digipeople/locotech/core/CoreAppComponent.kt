package ru.digipeople.locotech.core

import android.content.Context
import ru.digipeople.locotech.core.data.CorePrefs
import ru.digipeople.locotech.core.data.CoreThingWorxConfigProvider
import ru.digipeople.locotech.core.data.PlanBikUrlProvider
import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import ru.digipeople.locotech.core.helper.NotificationHelper
import ru.digipeople.locotech.core.push.service.AppFirebaseMessagingService
import ru.digipeople.locotech.core.push.service.FbcTokenManager
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import ru.digipeople.locotech.core.ui.helper.AuthInfoStorage

/**
 * Интерфейс модуля core
 */
interface CoreAppComponent {
    fun context(): Context

    fun inject(appFirebaseMessagingService: AppFirebaseMessagingService)
    /**
     * SharedPreference модуля core
     */
    fun corePrefs(): CorePrefs
    /**
     * Провайдер для конфига ThingWorx
     */
    fun thingWorxConfigProvider(): CoreThingWorxConfigProvider
    /**
     * Базовый навигатор
     */
    fun coreNavigator(): CoreNavigator
    /**
     * Провайдер для ссылки на План БИК
     */
    fun planBikUrlProvider(): PlanBikUrlProvider
    /**
     * Класс для хранения информации для авторизации
     */
    fun authInfoStorage(): AuthInfoStorage
    /**
     * Класс для создания/показа уведомлений от пушей
     */
    fun notificationHelper(): NotificationHelper
    /**
     * Класс, управляющий обновлением FirebaseCloud токена.
     */
    fun fbcTokenManager(): FbcTokenManager
    /**
     * Интерфейс для работы с платформой ThingWorx
     */
    fun coreThingsWorxWorker(): CoreThingsWorxWorker

    @ScreenOrientation
    fun screenOrientation(): Int

    companion object {
        private lateinit var instance: CoreAppComponent

        fun get(): CoreAppComponent = instance

        fun set(appComponent: CoreAppComponent) {
            instance = appComponent
        }
    }
}