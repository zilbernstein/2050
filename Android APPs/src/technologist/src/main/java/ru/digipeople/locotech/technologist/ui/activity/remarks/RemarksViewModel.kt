package ru.digipeople.locotech.technologist.ui.activity.remarks

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.technologist.model.Remark
import ru.digipeople.locotech.technologist.model.Work
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.locotech.technologist.ui.activity.remarks.adapter.remarks.AdapterData
import ru.digipeople.locotech.technologist.ui.activity.remarks.interactor.ApproveWorker
import ru.digipeople.locotech.technologist.ui.activity.remarks.interactor.EquipmentsLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewModel замечаний
 */
class RemarksViewModel @Inject constructor(
        private val equipmentsLoader: EquipmentsLoader,
        private val approveWorker: ApproveWorker,
        private val navigator: Navigator
) : BaseViewModel() {

    //region LiveData
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val worksNoDataLiveData = MutableLiveData<Boolean>()
    val remarksLiveData = MutableLiveData<AdapterData>()
    val selectedWorksLiveData = MutableLiveData<Map<String, Work>>()
    val allAreSelectedLiveData = MutableLiveData<Boolean>()
    val worksLiveData = MutableLiveData<List<Work>>()
    val buttonsEnabledLiveData = MutableLiveData<Boolean>()
    val acceptWorksApproveLiveData = SingleEventLiveData<List<String>>()
    val rejectWorksApproveLiveData = SingleEventLiveData<List<String>>()
    //endregion
    private var selectedRemark: Remark? = null
    private var selectedWorks = mutableMapOf<String, Work>()

    private var loadDataDisposable = Disposables.disposed()
    private var approveDisposable = Disposables.disposed()

    override fun onStart() {
        worksNoDataLiveData.value = true
        loadData()
    }
    /**
     * очистка
     */
    override fun onCleared() {
        super.onCleared()
        loadDataDisposable.dispose()
    }
    /**
     * Обработка нажатия на замечание
     */
    fun onRemarkClicked(remark: Remark) {
        selectedRemark = remark
        selectedWorks.clear()

        worksNoDataLiveData.value = false
        worksLiveData.value = remark.workList

        updateSelectedState()
    }
    /**
     * нажатие на принять работы
     */
    fun onWorksAcceptBtnClicked() {
        acceptWorksApproveLiveData.value = selectedWorks.map { it.value.name }
    }
    /**
     * смена статуса работы
     */
    fun onAcceptWorksApproveBtnClicked() {
        changeWorksState(false)
    }
    /**
     * нажатие на отклонить работы
     */
    fun onWorksRejectBtnClicked() {
        rejectWorksApproveLiveData.value = selectedWorks.map { it.value.name }
    }
    /**
     * Смена статуса работы
     */
    fun onRejectWorksApproveBtnClicked() {
        changeWorksState(true)
    }
    /**
     * Изменение статуса
     */
    fun onApproveCheckedChanged(work: Work) {
        if (selectedWorks.remove(work.id) == null) {
            selectedWorks[work.id] = work
        }
        updateSelectedState()
    }
    /**
     * выбрать все
     */
    fun onSelectAllBtnClicked() {
        if (selectedWorks.size == selectedRemark?.workList?.size ?: 0) {
            selectedWorks.clear()
        } else {
            selectedWorks.putAll(selectedRemark?.workList?.associateBy { it.id } ?: emptyMap())
        }
        updateSelectedState()
    }
    /**
     * нажатие на работу
     */
    fun onWorkClicked(work: Work) {
        navigator.navigateToWorkActivity(work.id)
    }
    /**
     * нажатие на фото
     */
    fun onPhotosMenuItemSelected() {
        selectedRemark?.let {
            navigator.navigateToPhotoGalleryActivity(it.id)
        }
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        loadDataDisposable.dispose()
        loadDataDisposable = equipmentsLoader.loadEquipments()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    remarksLiveData.value = result.equipments

                    // Reset ui state
                    selectedRemark = null
                    selectedWorks.clear()
                    worksNoDataLiveData.value = true
                    worksLiveData.value = emptyList()
                    /**
                     * Обрабока ошибки
                     */
                    if (!result.isSuccessful) {
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * Изменние статуса работы
     */
    private fun changeWorksState(reject: Boolean) {
        val remark = selectedRemark ?: return
        val workIds = selectedWorks.map { it.value.id }

        approveDisposable.dispose()
        approveDisposable = Single
                .defer {
                    if (reject) {
                        approveWorker.rejectWorks(remark.id, workIds)
                    } else {
                        approveWorker.approveWorks(remark.id, workIds)
                    }
                }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->/**
                 * Обработка результата
                 */
                    if (result.isSuccessful) {
                        loadData()
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        loadingLiveData.value = false
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    private fun updateSelectedState() {
        val works = selectedRemark?.workList ?: emptyList()
        allAreSelectedLiveData.value = selectedWorks.size == works.size
        selectedWorksLiveData.value = selectedWorks
        buttonsEnabledLiveData.value = selectedWorks.isNotEmpty()
    }
}