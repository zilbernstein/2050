package ru.digipeople.locotech.metrologist

import android.content.Context
import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import ru.digipeople.locotech.core.ui.helper.AuthInfoStorage
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.helper.AuthWorkerProxyImpl
import ru.digipeople.locotech.metrologist.helper.measurement.MeasurementFlowStorage
import ru.digipeople.locotech.metrologist.helper.session.SessionManager
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.base.MainDrawerDelegateFactoryImpl
import ru.digipeople.message.MessagesComponent
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton
/**
 * Интерфейс МП Мониторинг КП
 *
 * @author Kashonkov Nikita
 */
@Singleton
@Component(
        modules = [AppModule::class],
        dependencies = [
            UiComponent::class,
            CoreAppComponent::class,
            ThingWorxComponent::class,
            TelephoneBookComponent::class,
            MessagesComponent::class
        ]
)
interface AppComponent {

    fun context(): Context
    /**
     * Интерфейс для работы с платформой ThingWorx
     */
    fun coreThingsWorxWorker(): CoreThingsWorxWorker
    /**
     * Класс для хранения информации для авторизации
     */
    fun authInfoStorage(): AuthInfoStorage
    /**
     * обертка для метода авторизации
     */
    fun authWorkerProxy(): AuthWorkerProxyImpl
    /**
     * Класс дл работы с системой BaseThingWorx.
     */
    fun baseThingWorx(): BaseThingWorx
    /**
     * Интерфейс для [ThingsWorxWorker]
     */
    fun thingWorxWorker(): ThingsWorxWorker
    /**
     * Реализация интерфейса [MainDrawerDelegateFactory]
     */
    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactoryImpl
    /**
     * Класс осуществляющий навигацию между экранами
     */
    fun appNavigator(): AppNavigator
    /**
     * Менеджер пользователя, хранящий изменения id и названия выбранной секции
     */
    fun sessionManager(): SessionManager
    /**
     * Хранилище для значения замера при передаче его между экранами
     */
    fun measurementFlowStorage(): MeasurementFlowStorage

    companion object {
        private lateinit var instance: AppComponent

        fun set(component: AppComponent) {
            instance = component
        }

        fun get(): AppComponent = instance
    }
}