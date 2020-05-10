package ru.digipeople.locotech.inspector.ui.activity.signersearch

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.locotech.inspector.model.Signer
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter.SignerModel
import ru.digipeople.locotech.inspector.ui.activity.signersearch.interactor.SignersLoader
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер поиска подписантов
 *
 * @author Kashonkov Nikita
 */
class SignerSearchPresenter @Inject constructor(viewState: SignerSearchViewState,
                                                private val navigator: Navigator,
                                                private val signersLoader: SignersLoader,
                                                private val searchResultStorage: SingerSearchResultStorage) :
        BaseMvpViewStatePresenter<SignerSearchView, SignerSearchViewState>(viewState) {
    /**
     * подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(SignerSearchPresenter::class)

    private var loadDataDisposable: Disposable = Disposables.disposed()
    private val selectedItems = hashSetOf<Signer>()
    /**
     * инициализация
     */
    override fun onInitialize() {}

    fun searchChanged(search: String) {
        loadData(search)
    }
    /**
     * принть условия поиска
     */
    fun searchSubmitted(search: String) {
        loadData(search)
    }
    /**
     * нажатие на элемент
     */
    fun itemClicked(signerModel: SignerModel) {
        if (selectedItems.add(signerModel.signer)) {
            signerModel.isSelected = true
        } else {
            selectedItems.remove(signerModel.signer)
            signerModel.isSelected = false
        }
    }

    fun onScreenShown() {
        loadData("")
    }
    /**
     * обработка кнопки принять
     */
    fun onApplyBtnClicked() {
        searchResultStorage.putData(selectedItems)
        navigator.navigateBack()
    }
    /**
     * загрузка данных
     */
    private fun loadData(name: String) {
        loadDataDisposable.dispose()
        loadDataDisposable = signersLoader.loadSigners(name)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.setLoadingVisibility(false)
                            if (result.isSuccessful) {
                                view.setData(result.signers)
                            } else {
                                /**
                                 * обработка ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, { logger.error(it) })
    }


}