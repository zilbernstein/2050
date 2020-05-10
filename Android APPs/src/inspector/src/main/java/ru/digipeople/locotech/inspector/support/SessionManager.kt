package ru.digipeople.locotech.inspector.support

import io.reactivex.subjects.BehaviorSubject
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.core.helper.session.interactor.LogoutInteractor
import ru.digipeople.locotech.inspector.support.interactor.AuthInteractor
import ru.digipeople.locotech.inspector.support.model.SignInInfo
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Менеджер пользователя, хранящий изменения id и названия выбранной секции
 */
@Singleton
class SessionManager @Inject constructor(
        private val authInteractor: AuthInteractor,
        private val logoutInteractor: LogoutInteractor
) {
    val requireSignInInfo: SignInInfo
        get() = signInInfo ?: throw IllegalStateException("SignInInfo is null")
    val equipmentChanges = BehaviorSubject.create<ShortEquipment>()
    /**
     * состояние фильтра
     */
    var equipmentFilter = EquipmentFilter(emptyList())
    /**
     * выбранное оборудоавние
     */
    var selectedEquipment: ShortEquipment = ShortEquipment()
        private set
    var signInInfo: SignInInfo? = null
        private set
    /**
     * начать сессию
     */
    suspend fun startSession(username: String, password: String): UserError {
        val result = authInteractor.auth(username, password)
        if (result.isSuccessful) {
            signInInfo = result.authInfo
            val equipment = requireSignInInfo.availableEquipments.find {
                it.id == requireSignInInfo.selectedEquipmentId
            } ?: createEquipmentFromId(requireSignInInfo.selectedEquipmentId)
            updateSelectedEquipment(equipment)
        }
        return result.userError
    }
    /**
     * оборудование по id
     */
    private fun createEquipmentFromId(id: String): ShortEquipment {
        return ShortEquipment().apply { this.id = id }
    }
    /**
     * обновить выбранное оборудование
     */
    fun updateSelectedEquipment(equipment: ShortEquipment) {
        requireSignInInfo.selectedEquipmentId = equipment.id
        selectedEquipment = equipment
        equipmentChanges.onNext(selectedEquipment)
    }
    /**
     * закончить сессию
     */
    suspend fun endSession() {
        signInInfo = null
        logoutInteractor.logout()
    }
}