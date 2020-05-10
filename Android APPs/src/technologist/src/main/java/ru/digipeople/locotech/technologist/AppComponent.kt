package ru.digipeople.locotech.technologist

import android.content.Context
import dagger.Component
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.locotech.technologist.helper.AuthWorkerProxyImpl
import ru.digipeople.locotech.technologist.helper.ClientProvider
import ru.digipeople.locotech.technologist.ui.ActivityNavigator
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.locotech.technologist.ui.activity.AppNavigator
import ru.digipeople.locotech.technologist.ui.activity.base.MainDrawerDelegateFactoryImpl
import ru.digipeople.locotech.technologist.ui.activity.comment.CommentScreenComponent
import ru.digipeople.locotech.technologist.ui.activity.splash.SplashScreenComponent
import ru.digipeople.message.MessagesComponent
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.mvp.core.MvpProcessor
import javax.inject.Singleton

/**
 * Интерфейс МП Технолог
 *
 * @author Sostavkin Grisha
 */
@Singleton
@Component(modules = [AppModule::class], dependencies = [UiComponent::class, TelephoneBookComponent::class, ThingWorxComponent::class, MessagesComponent::class])
interface AppComponent {

    fun inject(appInitProvider: AppInitProvider)

    fun context(): Context
    /**
     * Класс, управляющий презентерами.
     */
    fun mvpProcessor(): MvpProcessor
    /**
     * Активити навигатор
     */
    fun activityNavigator(): ActivityNavigator
    /**
     * Провайдер текущего пользователя
     */
    fun clientProvider(): ClientProvider
    /**
     * Компонент экрана структуры замечаний
     */
    fun commentScreenComponent(): CommentScreenComponent
    /**
     * Класс дл работы с системой BaseThingWorx.
     */
    fun baseThingWorx(): BaseThingWorx
    /**
     * Экранный компонент структуры сплэш
     */
    fun splashScreenComponent(): SplashScreenComponent
    /**
     * Передает [ClientProvider] текущего пользователя
     */
    fun authWorkerProxy(): AuthWorkerProxyImpl
    /**
     * Реализация интерфейса [MainDrawerDelegateFactory]
     */
    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactoryImpl
    /**
     * Интерфейс для работы с Апи МП Технолог
     */
    fun thingsWorxWorker(): ThingsWorxWorker
    /**
     * Навигатор
     */
    fun appNavigator(): AppNavigator
    /**
     * Навигатор МП Технолог
     */
    fun navigator(): Navigator

    companion object {
        private lateinit var instance: AppComponent

        fun get(): AppComponent = instance

        fun set(appComponent: AppComponent) {
            instance = appComponent
        }
    }
}