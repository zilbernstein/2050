package ru.digipeople.telephonebook

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.digipeople.telephonebook.api.DefaultThingsWorxWorker
import ru.digipeople.telephonebook.api.ThingWorx
import ru.digipeople.telephonebook.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.ResponseConverter
import javax.inject.Singleton

/**
 * Модуль телефонный справочник
 *
 * @author Kashonkov Nikita
 */
@Module
class TelephoneBookModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun thingWorxWorker(thingWorx: ThingWorx, resposeConverter: ResponseConverter): ThingsWorxWorker {
        return DefaultThingsWorxWorker(thingWorx, resposeConverter)
    }
}