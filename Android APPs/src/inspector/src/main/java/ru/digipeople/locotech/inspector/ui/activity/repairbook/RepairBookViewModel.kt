package ru.digipeople.locotech.inspector.ui.activity.repairbook

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.core.ui.helper.AuthInfoStorage
import ru.digipeople.locotech.inspector.model.Document
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.repairbook.interactor.RepairWorkLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View Model книги ремонтов
 */
class RepairBookViewModel @Inject constructor(
        private val sessionManager: SessionManager,
        private val repairBookLoader: RepairWorkLoader,
        private val authInfoStorage: AuthInfoStorage
) : BaseViewModel() {
    //region LiveData
    val equipmentNameLiveData = MutableLiveData<String>()
    val selectedDocumentLiveData = MutableLiveData<Document>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val documentsLiveData = MutableLiveData<List<Document>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val authInfoLiveData = MutableLiveData<Pair<String, String>>()
    //endregion
    private var loadDocumentsDisposable = Disposables.disposed()

    override fun onStart() {
        equipmentNameLiveData.value = sessionManager.selectedEquipment.name
        authInfoLiveData.value = authInfoStorage.loginPassPair
        loadDocuments()
    }
    /**
     * очистка
     */
    override fun onCleared() {
        super.onCleared()
        loadDocumentsDisposable.dispose()
    }
    /**
     * загрузка документов
     */
    private fun loadDocuments() {
        loadDocumentsDisposable.dispose()
        loadDocumentsDisposable = repairBookLoader.getDocuments()
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    loadingLiveData.value = false
                    documentsLiveData.value = result.documents
                    noDataLiveData.value = result.documents.isEmpty()
                    result.documents.firstOrNull()?.let {
                        selectedDocumentLiveData.value = it
                    }
                    if (!result.isSuccessful) {
                        /**
                         * обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    fun onDocumentClicked(document: Document) {
        selectedDocumentLiveData.value = document
    }
}