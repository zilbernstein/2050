package ru.digipeople.qrscanner

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Модуль сканера
 *
 * @author Sostavkin Grisha
 */
@Module
open class ScannerModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }
}