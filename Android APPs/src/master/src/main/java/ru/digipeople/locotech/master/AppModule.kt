package ru.digipeople.locotech.master

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.digipeople.locotech.master.api.DefaultThingsWorxWorker
import ru.digipeople.locotech.master.api.ThingWorx
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.ResponseConverter
import javax.inject.Singleton
/**
 * Модуль приложения
 *
 * @author Kashonkov Nikita
 */
@Module
class AppModule(private val app: Application) {

    @Provides
    fun application(): Application = app

    @Provides
    @Singleton
    fun thingWorxWorker(thingWorx: ThingWorx, resposeConverter: ResponseConverter): ThingsWorxWorker {
        return DefaultThingsWorxWorker(thingWorx, resposeConverter)
    }
}