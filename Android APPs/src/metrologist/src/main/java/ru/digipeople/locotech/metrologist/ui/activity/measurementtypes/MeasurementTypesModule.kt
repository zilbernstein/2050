package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль типов замеров
 */
@Module
class MeasurementTypesModule(private val activity: Activity) {
    @Provides
    fun activity() = activity

    @Provides
    fun viewModelFactory(provider: Provider<MeasurementTypesViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return provider.get() as T
            }
        }
    }
}