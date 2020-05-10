package ru.digipeople.ui

import dagger.Module
import dagger.Provides
import ru.digipeople.ui.activity.base.BaseAuthorizationChecker
import ru.digipeople.ui.activity.base.MainDrawerDelegateFactory
import javax.inject.Singleton

/**
 * Модуль UI
 *
 * @author Kashonkov Nikita
 */
@Module
class UiModule(val authorizationChecker: BaseAuthorizationChecker,
               private val mainDrawerDelegateFactory: () -> MainDrawerDelegateFactory) {
    /**
     * Проверка авторизации
     */
    @Provides
    @Singleton
    fun authorizationChecker(): BaseAuthorizationChecker {
        return authorizationChecker
    }

    @Provides
    @Singleton
    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactory {
        return mainDrawerDelegateFactory.invoke()
    }

}