package ru.digipeople.locotech.core

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.digipeople.locotech.core.api.ThingWorxUrlProvider
import ru.digipeople.locotech.core.data.CoreThingWorxConfigProvider
import ru.digipeople.locotech.core.data.PlanBikUrlProvider
import ru.digipeople.locotech.core.data.api.CoreThingsWorxWorker
import ru.digipeople.locotech.core.data.api.DefaultCoreThingsWorxWorker
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import javax.inject.Qualifier

/**
 * Модуль core
 */
@Module
class CoreAppModule(private val app: Application,
                    private val library: String,
                    private val mashup: String = "",
                    private val screenOrientation: Int,
                    private val navigator: () -> CoreNavigator) {
    @Provides
    fun providesContext(): Context = app
    /**
     * Провайдер для конфига ThingWorx
     */
    @Provides
    fun coreThingWorxConfigProvider(thingWorxUrlProvider: ThingWorxUrlProvider) =
            CoreThingWorxConfigProvider(library, thingWorxUrlProvider)
    /**
     * Провайдер для ссылки на План БИК
     */
    @Provides
    fun planBikUrlProvider(thingWorxUrlProvider: ThingWorxUrlProvider) =
            PlanBikUrlProvider(thingWorxUrlProvider, mashup)
    /**
     * Базовый навигатор
     */
    @Provides
    fun navigator(): CoreNavigator {
        return navigator.invoke()
    }
    /**
     * Интерфейс для работы с платформой ThingWorx
     */
    @Provides
    fun coreThingsWorxWorker(coreThingsWorxWorker: DefaultCoreThingsWorxWorker): CoreThingsWorxWorker {
        return coreThingsWorxWorker
    }

    @Provides
    @ScreenOrientation
    fun screenOrientation(): Int = screenOrientation
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ScreenOrientation