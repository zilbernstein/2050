package ru.digipeople.locotech.worker

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ActivityInfo
import android.database.Cursor
import android.net.Uri
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import io.reactivex.plugins.RxJavaPlugins
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.CoreAppModule
import ru.digipeople.locotech.core.DaggerCoreAppComponentImpl
import ru.digipeople.locotech.core.helper.Flavor
import ru.digipeople.locotech.core.helper.NotificationHelper
import ru.digipeople.locotech.core.push.service.FbcTokenManager
import ru.digipeople.locotech.core.ui.activity.auth.AuthParams
import ru.digipeople.locotech.worker.helper.AuthorizationChecker
import ru.digipeople.locotech.worker.helper.RxErrorHandler
import ru.digipeople.message.DaggerMessagesComponentImpl
import ru.digipeople.message.MessagesComponent
import ru.digipeople.photo.DaggerPhotoComponentImpl
import ru.digipeople.photo.PhotoComponent
import ru.digipeople.photo.PhotoModule
import ru.digipeople.telephonebook.DaggerTelephoneBookComponentImpl
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.telephonebook.TelephoneBookModule
import ru.digipeople.thingworx.DaggerThingWorxComponentImpl
import ru.digipeople.thingworx.ThingWorxModule
import ru.digipeople.ui.DaggerUiComponentImpl
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.UiModule
import javax.inject.Inject

/**
 * Класс отвечающий за инициализацию приложения
 *
 * @author Kashonkov Nikita
 */
class AppInitProvider : ContentProvider() {

    @Inject
    lateinit var fbcTokenManager: FbcTokenManager

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate(): Boolean {
        initDI(context)
        initAppCenter(context)
        AppComponent.get().inject(this)
        initRxPlugins()
        initFirebase()
        initNotifications()

        if (!Flavor.isDev) {

        }

        return false
    }
    /**
     * Инициализация AppCenter
     */
    private fun initAppCenter(appContext: Context) {
        val app = appContext as Application
        AppCenter.start(
                app, BuildConfig.APP_CENTER_SECRET,
                Analytics::class.java,
                Crashes::class.java,
                Distribute::class.java
        )
    }
    /**
     * инициализация даггера
     */
    private fun initDI(appContext: Context) {
        val app = appContext as Application
        // UiComponent
        val uiModule = UiModule(AuthorizationChecker(), { AppComponent.get().mainDrawerDelegateFactory() })
        val uiComponent = DaggerUiComponentImpl
                .builder()
                .uiModule(uiModule)
                .build()
        UiComponent.set(uiComponent)
        // ThingWorxComponent
        val thingWorxModule = ThingWorxModule { CoreAppComponent.get().thingWorxConfigProvider() }
        val thingWorxComponent = DaggerThingWorxComponentImpl
                .builder()
                .thingWorxModule(thingWorxModule)
                .build()
        // CoreComponent
        val coreAppModule = CoreAppModule(
                app = app,
                library = BuildConfig.THINGWORX_LIBRARY,
                mashup = BuildConfig.MASHUP,
                screenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT,
                navigator = { AppComponent.get().appNavigator() })
        val coreAppComponent = DaggerCoreAppComponentImpl
                .builder()
                .coreAppModule(coreAppModule)
                .thingWorxComponent(thingWorxComponent)
                .build()
        CoreAppComponent.set(coreAppComponent)
        // Photo
        val photoModule = PhotoModule(appContext)
        val photoComponent = DaggerPhotoComponentImpl
                .builder()
                .uiComponent(uiComponent)
                .thingWorxComponent(thingWorxComponent)
                .photoModule(photoModule)
                .build()
        PhotoComponent.set(photoComponent)
        // MessagesComponent
        val messagesComponent = DaggerMessagesComponentImpl
                .builder()
                .uiComponent(uiComponent)
                .coreAppComponent(coreAppComponent)
                .thingWorxComponent(thingWorxComponent)
                .build()
        MessagesComponent.set(messagesComponent)
        //TelephoneBookComponent
        val telephoneBookModule = TelephoneBookModule(appContext)
        val telephoneBookComponent = DaggerTelephoneBookComponentImpl
                .builder()
                .uiComponent(uiComponent)
                .thingWorxComponent(thingWorxComponent)
                .telephoneBookModule(telephoneBookModule)
                .build()
        TelephoneBookComponent.set(telephoneBookComponent)
        val appComponent = DaggerAppComponentImpl
                .builder()
                .uiComponent(uiComponent)
                .thingWorxComponent(thingWorxComponent)
                .messagesComponent(messagesComponent)
                .appModule(AppModule(app))
                .photoComponent(photoComponent)
                .build()
        AppComponent.set(appComponent)
        AppComponent.get().inject(this)

        AppComponent.get().appNavigator().init()

        AuthParams.apply {
            debugCredentials = Pair("слесарь2", "0000")
            specializationTitleText = "Сотрудник"

            authWorkerProxy = appComponent.authWorkerProxy()
            onSuccess = { activity -> appComponent.activityNavigator().navigateToSplashActivity(activity) }
        }
    }

    private fun initRxPlugins() {
        RxJavaPlugins.setErrorHandler(RxErrorHandler())
    }
    /**
     * Инициализация FireBase
     */
    private fun initFirebase() = fbcTokenManager.updateFbcTokenAsync()
    /**
     * Инициализация уведомлений
     */
    private fun initNotifications() = notificationHelper.subscribeToNotificationsAsync()

    override fun insert(uri: Uri?, values: ContentValues?): Uri? = null

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun getType(uri: Uri?): String? = null
}