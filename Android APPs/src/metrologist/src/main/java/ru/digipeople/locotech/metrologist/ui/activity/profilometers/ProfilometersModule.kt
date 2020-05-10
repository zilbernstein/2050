package ru.digipeople.locotech.metrologist.ui.activity.profilometers

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль профилометров
 */
@Module
class ProfilometersModule(private val activity: Activity) {
    @Provides
    fun viewModelFactory(viewModelProvider: Provider<ProfilometersViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}