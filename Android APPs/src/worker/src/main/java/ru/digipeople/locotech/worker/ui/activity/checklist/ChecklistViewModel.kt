package ru.digipeople.locotech.worker.ui.activity.checklist

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.worker.interactor.CheckListLoader
import ru.digipeople.locotech.worker.model.Checklist
import ru.digipeople.locotech.worker.model.ChecklistItem
import ru.digipeople.locotech.worker.ui.activity.AppNavigator
import ru.digipeople.locotech.worker.ui.activity.checklist.interactor.ChecklistToggleInteractor
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewModel для структуры чеклист
 *
 * @author Sostavkin Grisha
 */
class ChecklistViewModel @Inject constructor(private val checkListLoader: CheckListLoader,
                                             private val checklistToggleInteractor: ChecklistToggleInteractor,
                                             private val appNavigator: AppNavigator)
    : BaseViewModel() {
    //region Params
    lateinit var workId: String
    //endregion
    //region LiveData
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    var checklistLiveData = MutableLiveData<List<ChecklistItem>>()
    //endregion
    private var checklistDisposable: Disposable = Disposables.disposed()
    private var toggleDisposable: Disposable = Disposables.disposed()
    private var checklist: Checklist? = null
    /**
     * Действия при старте
     */
    override fun onStart() {
        loadData()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        checklistDisposable.dispose()
        toggleDisposable.dispose()
        super.onCleared()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        checklistDisposable = checkListLoader.loadChecklist(workId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        noDataLiveData.value = result.checklist.items.isEmpty()
                        checklist = result.checklist
                        checklistLiveData.value = result.checklist.items
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        noDataLiveData.value = true
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
    /**
     * Выбор пункта чеклиста
     */
    fun onToggleChecklistItem(checklistItem: ChecklistItem) {
        toggleDisposable = checklistToggleInteractor.toggleChecklistItem(workId, checklistItem)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        checklistItem.isChecked = !checklistItem.isChecked
                        checklistLiveData.value = checklistLiveData.value
                    } else {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    fun onTechClicked() {
        checklist?.let { appNavigator.navigateToPdfLink(it.techProcessUrl) }
    }
}