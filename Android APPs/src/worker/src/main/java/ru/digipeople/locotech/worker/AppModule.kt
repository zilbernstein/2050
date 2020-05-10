package ru.digipeople.locotech.worker

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.locotech.worker.api.DefaultThingsWorxWorker
import ru.digipeople.locotech.worker.api.ThingWorx
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import javax.inject.Singleton

/**
 * Модуль приложения
 *
 * @author Kashonkov Nikita
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