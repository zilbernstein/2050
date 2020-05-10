package ru.digipeople.locotech.technologist

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.digipeople.locotech.technologist.api.DefaultThingsWorxWorker
import ru.digipeople.locotech.technologist.api.ThingWorx
import ru.digipeople.locotech.technologist.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.ResponseConverter
import javax.inject.Singleton

/**
 * Модуль МП Технолог
 *
 * @author Sostavkin Grisha
 */
@Module
class AppModule(private val app: Application) {

    @Provides
    fun context(): Context = app

    @Provides
    fun application(): Application = app

    @Provides
    @Singleton
    fun thingWorxWorker(thingWorx: ThingWorx, resposeConverter: ResponseConverter): ThingsWorxWorker {
        return DefaultThingsWorxWorker(thingWorx, resposeConverter)
    }
}