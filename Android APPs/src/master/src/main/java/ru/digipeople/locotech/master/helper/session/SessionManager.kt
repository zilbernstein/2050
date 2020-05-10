package ru.digipeople.locotech.master.helper.session

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.core.helper.session.interactor.LogoutInteractor
import ru.digipeople.locotech.master.helper.session.interactor.AuthInteractor
import ru.digipeople.locotech.master.helper.session.model.SignInInfo
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Менеджер пользователя, хранящий изменения id и названия выбранной секции
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class SessionManager @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val logoutInteractor: LogoutInteractor
) {
    private var _signInInfo: SignInInfo? = null
    val signInInfo
        get() = _signInInfo!!

    //TODO add session state here for selected repair cites and selection menu

    /**
     * Выбранное оборудование
     */
    private var _selectedEquipment: ShortEquipment? = null
    val selectedEquipment
        get() = _selectedEquipment
    /**
     * Доступное для выбора оборудование (секции)
     */
    private var _availableEquipments = emptyList<ShortEquipment>()
    val availableEquipments
        get() = _availableEquipments

    var equipmentFilter: EquipmentFilter = EquipmentFilter(emptyList())
    /**
     * Измененное оборудование (секции)
     */
    private val _equipmentChanges = BehaviorSubject.create<Unit>()
    val equipmentChanges: Observable<Unit> = _equipmentChanges.observeOn(AppSchedulers.ui())

    val isAuthenticated
        get() = _signInInfo != null

    /**
     * Обработка старта сессии
     */
    suspend fun startSession(username: String, password: String): UserError {
        val result = authInteractor.auth(username, password)
        if (result.isSuccessful) {
            _signInInfo = result.authInfo
            updateSelectedEquipment(signInInfo.selectedEquipmentId, signInInfo.availableEquipments)
        }
        return result.userError
    }
    /**
     * Обработка изменения выбранного оборудования (секции)
     */
    fun updateSelectedEquipment(selectedEquipmentId: String, availableEquipments: List<ShortEquipment>) {
        val selectedEquipment = availableEquipments.firstOrNull { it.id == selectedEquipmentId }
        this._availableEquipments = availableEquipments
        this._selectedEquipment = selectedEquipment
        _equipmentChanges.onNext(Unit)
    }

    /**
     * Обработка завершания сессии
     */
    suspend fun endSession() {
        _signInInfo = null
        logoutInteractor.logout()
    }
}