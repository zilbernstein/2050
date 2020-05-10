package ru.digipeople.locotech.master.ui.activity.assignmenttemplates

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter.item.AssignmentTemplateItem
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.interactor.AssignmentTemplateSetInteractor
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.interactor.AssignmentTemplatesLoader
import ru.digipeople.utils.model.UserError
import java.util.*
import javax.inject.Inject
/**
 * View model выбора шаблона исполнителей
 */
class AssignmentTemplatesViewModel @Inject constructor(
        private val assignmentTemplatesLoader: AssignmentTemplatesLoader,
        private val assignmentTemplateSetInteractor: AssignmentTemplateSetInteractor
) : BaseViewModel() {
    /**
     * LiveData для передачи данных
     */
    val assignmentTemplatesLiveData = MutableLiveData<List<AssignmentTemplateItem>>()
    val noDataLiveData = SingleEventLiveData<Boolean>()
    val assignmentWasSetLiveData = SingleEventLiveData<Unit>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = MutableLiveData<UserError>()

    private var assignmentTemplatesDisposable = Disposables.disposed()
    private var assignmentSetDisposable = Disposables.disposed()
    /**
     * Операции, выполняемые при старте
     */
    override fun onStart() {
        loadAssignmentTemplates()
    }

    override fun onCleared() {
        assignmentTemplatesDisposable.dispose()
        assignmentSetDisposable.dispose()
        super.onCleared()
    }
    /**
     * Загрузка шаблонов исполнителей
     */
    private fun loadAssignmentTemplates() {
        assignmentTemplatesDisposable = assignmentTemplatesLoader
                .loadAssignmentTemplates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        noDataLiveData.value = result.assignmentTemplates.isEmpty()
                        assignmentTemplatesLiveData.value = result.assignmentTemplates
                    } else {
                        /**
                         * Сообщение об ошибке
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, {
                    loadingLiveData.value = false
                    logger.error(it)
                })
    }
    /**
     * Установка шаблона
     */
    fun onSetTemplateClicked(item: AssignmentTemplateItem, date: Date, nightShift: Boolean) {
        assignmentSetDisposable = assignmentTemplateSetInteractor
                .setAssignmentTemplate(item, date, nightShift)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        assignmentWasSetLiveData.value = Unit
                    } else {
                        /**
                         * Сообщение об ошибке
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, {
                    loadingLiveData.value = false
                    logger.error(it)
                })
    }
}