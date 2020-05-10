package ru.digipeople.photo

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.digipeople.photo.api.DefaultThingWorxWorker
import ru.digipeople.photo.api.ThingWorx
import ru.digipeople.photo.api.ThingWorxWorker
import ru.digipeople.photo.helper.PhotoFileStorage
import ru.digipeople.photo.helper.PhotoFileStorageImpl
import ru.digipeople.thingworx.helper.ResponseConverter
import javax.inject.Singleton

/**
 * Модуль фото
 *
 * @author Kashonkov Nikita
 */
@Module
class PhotoModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun thingWorxWorker(thingWorx: ThingWorx, resposeConverter: ResponseConverter): ThingWorxWorker {
        return DefaultThingWorxWorker(thingWorx, resposeConverter)
    }

    @Provides
    @Singleton
    fun photoFileStorage(): PhotoFileStorage{
        return PhotoFileStorageImpl(context)
    }
}