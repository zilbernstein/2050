package ru.digipeople.message

import dagger.Module
import dagger.Provides
import ru.digipeople.message.api.DefaultThingsWorxWorker
import ru.digipeople.message.api.ThingWorx
import ru.digipeople.message.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.ResponseConverter
import javax.inject.Singleton

/**
 * Модуль сообщений
 */
@Module
class MessagesModule {
    @Provides
    @Singleton
    fun thingWorxWorker(thingWorx: ThingWorx, resposeConverter: ResponseConverter): ThingsWorxWorker {
        return DefaultThingsWorxWorker(thingWorx, resposeConverter)
    }
}