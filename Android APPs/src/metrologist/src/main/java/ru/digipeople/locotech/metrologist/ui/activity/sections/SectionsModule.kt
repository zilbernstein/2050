package ru.digipeople.locotech.metrologist.ui.activity.sections

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль секций
 */
@Module
class SectionsModule(private val activity: Activity) {
    @Provides
    //fun activity() = activity

    fun viewModelFactory(viewModelProvider: Provider<SectionsViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}
