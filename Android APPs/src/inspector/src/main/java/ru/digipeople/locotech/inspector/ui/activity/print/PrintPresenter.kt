package ru.digipeople.locotech.inspector.ui.activity.print

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.inspector.helper.UserErrorBuilder
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.DocumentModel
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.SignersCategoryModel
import ru.digipeople.locotech.inspector.ui.activity.print.interactor.DocumentsLoader
import ru.digipeople.locotech.inspector.ui.activity.print.interactor.EmailForPrintSaver
import ru.digipeople.locotech.inspector.ui.activity.print.interactor.PrintWorker
import ru.digipeople.locotech.inspector.ui.activity.print.interactor.SignersCategoriesLoader
import ru.digipeople.locotech.inspector.ui.activity.signersearch.SingerSearchResultStorage
import ru.digipeople.locotech.inspector.ui.helper.ClientProvider
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.thingworx.model.SignersCategoryEntity
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Презентер печати
 *
 * @author Kashonkov Nikita
 */
class PrintPresenter @Inject constructor(
        viewState: PrintViewState,
        private val sessionManager: SessionManager,
        private val navigator: Navigator,
        private val documentsLoader: DocumentsLoader,
        private val signersCategoriesLoader: SignersCategoriesLoader,
        private val emailForPrintSaver: EmailForPrintSaver,
        private val printWorker: PrintWorker,
        private val singerSearchResultStorage: SingerSearchResultStorage,
        private val errorBuilder: UserErrorBuilder
) : BaseMvpViewStatePresenter<PrintView, PrintViewState>(viewState) {
    /**
     * подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(PrintPresenter::class)

    private var loadDocumentsDisposable: Disposable = Disposables.disposed()
    private var loadSignersDisposable: Disposable = Disposables.disposed()
    private var singerSearchDisposable: Disposable = Disposables.disposed()
    private var printDisposable: Disposable = Disposables.disposed()
    private var saveEmailDisposable = Disposables.disposed()

    private var documents = emptyList<DocumentModel>()
    private var signersCatagories = mutableListOf<SignersCategoryModel>()
    private var countCheckedDocuments = 0
    /**
     * инициализация
     */
    override fun onInitialize() {
        loadDocuments()
        loadSigners()
    }

    override fun destroy() {
        loadDocumentsDisposable.dispose()
        singerSearchDisposable.dispose()
        saveEmailDisposable.dispose()
        super.destroy()
    }
    /**
     * выбрать все
     */
    fun onCheckAllClicked(isCheked: Boolean) {
        documents.forEach {
            it.isSelected = isCheked
        }
        if (isCheked) {
            countCheckedDocuments = documents.count()
        } else {
            countCheckedDocuments = 0
        }
        compareCheckedDocsCount()
        view.notifyDataSetChanged()
    }
    /**
     * управление заголовком выбрать все
     */
    fun compareCheckedDocsCount() {
        if (countCheckedDocuments < documents.count()) {
            view.setTitleCheckAll(true)
        } else {
            view.setTitleCheckAll(false)
        }
    }
    /**
     * очистка списка подписантов
     */
    fun onClearAllSignersClicked() {
        view.showDeleteSignersDialog()
    }

    fun onClearAllSignersApproveClicked() {
        signersCatagories.forEach {
            it.signers.clear()
        }
        view.notifyDataSetChanged()
    }
    /**
     * обработка нажатия на документ
     */
    fun onDocumentClicked(document: DocumentModel) {
        document.isSelected = !document.isSelected
        if (document.isSelected) {
            countCheckedDocuments++
        } else {
            countCheckedDocuments--
        }
        compareCheckedDocsCount()
        view.notifyDataSetChanged()
    }
    /**
     * удаление подписанта
     */
    fun onRemoveSigner(signersCategory: SignersCategoryModel, signer: ru.digipeople.locotech.inspector.ui.activity.print.adapter.Signer) {
        signersCategory.signers.remove(signer)
        view.notifyDataSetChanged()
    }
    /**
     * удаление всех подписанитов из категории
     */
    fun onRemoveAllSignersFromCategory(signersCategoryModel: SignersCategoryModel) {
        signersCategoryModel.signers.clear()
        view.notifyDataSetChanged()
    }
    /**
     * добавление подписанита в категорию
     */
    fun onAddSignerToCategory(signersCategoryModel: SignersCategoryModel) {
        singerSearchDisposable.dispose()
        singerSearchDisposable = singerSearchResultStorage.dataChanges().subscribe { addedSigners ->
            addedSigners.forEach { addedSigner ->
                val hasSigner = signersCategoryModel.signers.find { it.id.equals(addedSigner.id) } // проверка, есть ли уже добавляемый элемент в списке
                if (hasSigner == null) {
                    val signer = ru.digipeople.locotech.inspector.ui.activity.print.adapter.Signer()
                    signer.id = addedSigner.id
                    signer.name = addedSigner.name
                    signersCategoryModel.signers.add(signer)
                }
            }
            loadSignersDisposable.dispose()
            view.notifyDataSetChanged()
        }
        navigator.navigateToSignerSearch()
    }
    /**
     * обработка печати
     */
    fun onPrintBtnClicked() {
        var countCheckDocs = 0
        var countSigners = 0

        countCheckDocs = documents.count { it.isSelected }

        signersCatagories.forEach { signersCategory ->
            signersCategory.signers.forEach {
                countSigners++
            }
        }

        if (countCheckDocs == 0) {
            view.showError(errorBuilder.noDocumentsError)
            return
        }

        if (countSigners == 0) {
            view.showError(errorBuilder.noSignersError)
            return
        }

        view.showEnterEmailDialog(sessionManager.requireSignInInfo.emailForPrint!!)
    }

    fun onScreenShown() {
        view.setTitle(sessionManager.selectedEquipment.name)
    }

    fun onSendBtnClicked(email: String) {
        val documentsForPrint = documents.filter { it.isSelected }.map { it.id }
        val signerCategories =
                signersCatagories.map { signerCategory ->
                    /**
                     * обработка результата
                     */
                    SignersCategoryEntity().apply {
                        id = signerCategory.id
                        signers = signerCategory.signers.map { it.id }
                    }
                }

        if (email != sessionManager.requireSignInInfo.emailForPrint) {
            saveEmailDisposable.dispose()
            saveEmailDisposable = emailForPrintSaver.save(email)
                    .subscribeOn(AppSchedulers.io())
                    .observeOn(AppSchedulers.ui())
                    .subscribe({ userError ->
                        /**
                         * обработка ошибки
                         */
                        if (userError == UserError.NO_ERROR) {
                            logger.trace("Email: $email saved successfully")
                            sessionManager.requireSignInInfo.emailForPrint = email
                        } else logger.error("Email: $email is not saved")
                    }, { throwable ->
                        logger.error("Email: $email is not saved in cause: ${throwable.message}")
                    })
        }

        printDisposable.dispose()
        printDisposable = printWorker.sendToPrint(email, documentsForPrint, signerCategories)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        navigator.navigateBack()
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError)
                    }
                }, {
                    logger.error(it)
                })
    }
    /**
     * загрузка документов
     */
    private fun loadDocuments() {
        loadDocumentsDisposable.dispose()
        loadDocumentsDisposable = documentsLoader.loadDocuments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            if (result.isSuccessful) {
                                this.documents = result.documents
                                view.setDocuments(result.documents)
                            } else {
                                /**
                                 * обработка ошибки
                                 */
                                view.showError(result.userError)
                            }
                        }, { logger.error(it) }
                )
    }
    /**
     * загрузка подписантов
     */
    private fun loadSigners() {
        loadSignersDisposable.dispose()
        loadSignersDisposable = signersCategoriesLoader.loadSignersCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            if (result.isSuccessful) {
                                signersCatagories = result.signersCatagories.toMutableList()
                                view.setSignersCategories(result.signersCatagories)
                            } else {
                                /**
                                 * обработка ошибки
                                 */
                                view.showError(result.userError)
                            }
                        }, { logger.error(it) }
                )
    }
}