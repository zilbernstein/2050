package ru.digipeople.locotech.metrologist.helper.session

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ru.digipeople.locotech.metrologist.data.model.AuthInfo
import ru.digipeople.locotech.metrologist.helper.session.interactor.AuthInteractor
import ru.digipeople.locotech.metrologist.helper.session.interactor.LogoutInteractor
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Менеджер пользователя, хранящий изменения id и названия выбранной секции
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class SessionManager @Inject constructor(private val authInteractor: AuthInteractor,
                                         private val logoutInteractor: LogoutInteractor) {

    var authInfo: AuthInfo? = null

    private var _currentSectionId = ""
    val currentSectionId
        get() = _currentSectionId
    private var _currentSectionName = ""
    val currentSectionName
        get() = _currentSectionName
    /**
     * Фильтр оборудования
     */
    var equipmentFilter: EquipmentFilter = EquipmentFilter(emptyList())
    /**
     * Изменнеи текцщей секции
     */
    private val _currentSectionChanges = BehaviorSubject.create<Unit>()
    val currentSectionChanges: Observable<Unit> = _currentSectionChanges

    val isAuthenticated
        get() = authInfo != null
    /**
     * Начало сессии
     */
    fun startSession(username: String, password: String): Single<UserError> {
        return authInteractor.auth(username, password)
                .doOnSuccess { result ->
                    /**
                     * Обработка результата
                     */
                    if (result.isSuccessful) {
                        authInfo = result.authInfo
                        _currentSectionId = authInfo!!.currentSectionId
                        _currentSectionName = authInfo!!.currentSectionName
                        _currentSectionChanges.onNext(Unit)
                    }
                }
                .map { it.userError }
    }
    /**
     * Выбор секции
     */
    fun setCurrentSection(id: String, name: String) {
        _currentSectionId = id
        _currentSectionName = name
        _currentSectionChanges.onNext(Unit)
    }
    /**
     * Завершение сессии
     */
    fun endSession() {
        logoutInteractor.logout()
    }
}