package ru.digipeople.locotech.master.ui.activity.assignmenttemplates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль выбора шаблона исполнителей
 */
@Module
class AssignmentTemplatesModule {
    @Provides
    fun viewModelFactory(viewModelProvider: Provider<AssignmentTemplatesViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}