package ru.digipeople.locotech.master.ui.activity.ordertorepair

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.locotech.master.ui.activity.ordertorepair.interactor.OrderToRepairWorker
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View model запрос на получение ТМЦ для ремонта
 */
class OrderToRepairViewModel @Inject constructor(
        private val orderToRepairWorker: OrderToRepairWorker,
        private val sessionManager: SessionManager
) : BaseViewModel() {
    //region LiveData
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val statusLiveData = SingleEventLiveData<String>()
    //endregion
    private var orderToRepairDisposable = Disposables.disposed()
    /**
     * действия при старте
     */
    override fun onStart() {
        // nop
    }
    /**
     * очистка
     */
    override fun onCleared() {
        orderToRepairDisposable.dispose()
        super.onCleared()
    }
    /**
     * обработка нажатия на кнопку отправить запрос
     */
    fun onSendButtonClicked() {
        val sectionId = sessionManager.selectedEquipment?.id
        if (!sectionId.isNullOrEmpty()) {
            setOrderToRepair(sectionId)
        }
    }
    /**
     * запрос на получение ТМЦ для проведения ремонтных работ
     */
    private fun setOrderToRepair(sectionId: String) {
        orderToRepairDisposable.dispose()
        orderToRepairDisposable = orderToRepairWorker.setOrderToRepair(sectionId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обработа результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        statusLiveData.value = result.status
                    } else {
                        /**
                         * сообщение об ошибке
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}