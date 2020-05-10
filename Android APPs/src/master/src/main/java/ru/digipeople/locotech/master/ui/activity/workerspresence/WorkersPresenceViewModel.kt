package ru.digipeople.locotech.master.ui.activity.workerspresence

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.master.ui.activity.AppNavigator
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.WorkerPresenceItem
import ru.digipeople.locotech.master.ui.activity.workerspresence.interactor.BrigadesPresenceLoader
import ru.digipeople.locotech.master.ui.activity.workerspresence.interactor.WorkerPresenceChangeInteractor
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View model явки сотрудников
 */
class WorkersPresenceViewModel @Inject constructor(
        private val navigator: AppNavigator,
        private val brigadesPresenceLoader: BrigadesPresenceLoader,
        private val workerPresenceChangeInteractor: WorkerPresenceChangeInteractor
) : BaseViewModel() {
    val noDataLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val brigadesLiveData = MutableLiveData<List<Any>>()
    val editableItemLiveData = MutableLiveData<WorkerPresenceItem>()

    private var brigadesPresenceDisposable = Disposables.disposed()
    private var changeWorkerPresenceDisposable = Disposables.disposed()
    /**
     * Действия при старте класса
     */
    override fun onStart() {
        loadBrigadesPresence()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        brigadesPresenceDisposable.dispose()
        changeWorkerPresenceDisposable.dispose()
    }
    /**
     * Обработка редактирования явки
     */
    fun onEditBtnClicked(item: WorkerPresenceItem) {
        if (editableItemLiveData.value != null) return
        editableItemLiveData.value = item.copy()
    }
    /**
     * Обработка нажатия кнопки сохранить
     */
    fun onSaveBtnClicked(item: WorkerPresenceItem, position: Int) {
        val sourceItem = brigadesLiveData.value?.get(position)
        if (sourceItem == item) {
            editableItemLiveData.value = null
            return
        }

        changeWorkerPresenceDisposable.dispose()
        changeWorkerPresenceDisposable = workerPresenceChangeInteractor
                .changeWorkerPresence(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        val resultItem = result.workerPresenceItem
                        val brigadesData = brigadesLiveData.value?.toMutableList()
                        brigadesData?.set(position, item.copy(
                                presence = resultItem.presence,
                                timeIn = resultItem.timeIn,
                                timeOut = resultItem.timeOut,
                                timeBegin = resultItem.timeBegin,
                                timeFinish = resultItem.timeFinish,
                                workTime = resultItem.workTime,
                                timeNight = resultItem.timeNight,
                                night = resultItem.night
                        ))
                        editableItemLiveData.value = null
                        brigadesLiveData.value = brigadesData
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, logger::error)
    }
    /**
     * Обрабокта нажатия кнопки назад
     */
    fun onBackPressed() {
        return if (editableItemLiveData.value != null) {
            editableItemLiveData.value = null
        } else {
            navigator.navigateBack()
        }
    }
    /**
     * Обработка отмены установки явки
     */
    fun onCancelClicked() {
        if (editableItemLiveData.value == null) return
        editableItemLiveData.value = null
    }
    /**
     * Загрузка списка явки
     */
    private fun loadBrigadesPresence() {
        brigadesPresenceDisposable = brigadesPresenceLoader
                .loadPresence()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        brigadesLiveData.value = result.brigadesItems
                        noDataLiveData.value = false
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                        noDataLiveData.value = true
                    }
                }, logger::error)
    }
}