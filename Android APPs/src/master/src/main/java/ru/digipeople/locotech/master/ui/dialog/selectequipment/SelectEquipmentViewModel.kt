package ru.digipeople.locotech.master.ui.dialog.selectequipment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.locotech.master.interactor.SelectEquipmentInteractor
import ru.digipeople.locotech.master.ui.activity.AppNavigator
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View model диалога выбора оборудования из бокового меню.
 */
class SelectEquipmentViewModel @Inject constructor(
        private val sessionManager: SessionManager,
        private val selectEquipmentInteractor: SelectEquipmentInteractor,
        private val appNavigator: AppNavigator
) : BaseViewModel() {
    //region LiveData
    val equipmentsLiveData = MutableLiveData<List<ShortEquipment>>()
    val selectedEquipmentIdLiveData = MutableLiveData<String>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val dismissEventLiveData = SingleEventLiveData<Unit>()
    val loadingLiveData = MutableLiveData<Boolean>()
    //endregion
    /**
     * Действия при старте
     */
    override fun onStart() {
        equipmentsLiveData.value = sessionManager.availableEquipments
        sessionManager.selectedEquipment?.let {
            selectedEquipmentIdLiveData.value = it.id
        }
        loadingLiveData.value = false
    }
    /**
     * Обработка нажатия на оборудование
     */
    fun onEquipmentClicked(equipment: ShortEquipment) {
        if (equipment.id == sessionManager.selectedEquipment?.id) {
            dismissEventLiveData.value = Unit
            return
        }
        loadingLiveData.value = true
        viewModelScope.launch {
            val result = selectEquipmentInteractor.selectEquipment(equipment.id)
            if (result.isSuccessful) {
                appNavigator.navigateToPerformance()
            } else {
                /**
                 * Обработка ошибки
                 */
                loadingLiveData.value = false
                userErrorLiveData.value = result.userError
            }
        }
    }
}