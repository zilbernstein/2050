package ru.digipeople.locotech.master.ui.activity.workerspresence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
/**
 * Модуль явки сотрудников
 */
@Module
class WorkersPresenceModule {
    @Provides
    fun viewModelFactory(viewModelProvider: Provider<WorkersPresenceViewModel>): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelProvider.get() as T
            }
        }
    }
}