package ru.digipeople.locotech.inspector.ui.activity.remarksearch

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.inspector.model.RemarkFromCatalog
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.remarksearch.interactor.RemarkSearchLoader
import ru.digipeople.locotech.inspector.ui.activity.remarksearch.interactor.RemarkSearchWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер добавления/выбора замечаний
 * @author Kashonkov Nikita
 */
class RemarkSearchPresenter @Inject constructor(viewState: RemarkSearchViewState,
                                                private val navigator: Navigator,
                                                private val remarkLoader: RemarkSearchLoader,
                                                private val remarkWorker: RemarkSearchWorker) : BaseMvpViewStatePresenter<RemarkSearchView, RemarkSearchViewState>(viewState) {
    //region other
    private lateinit var list: List<RemarkFromCatalog>
    private var searchRemarkDisposable = Disposables.disposed()
    private var customRemarkDisposable = Disposables.disposed()
    private var chooseRemarkDisposable = Disposables.disposed()
    private var startTab = 0
    //endRegion
    /**
     * инициализация
     */
    override fun onInitialize() {}

    fun searchChanged(search: String) {
        filterList(search)
    }
    /**
     * поиск по строке
     */
    fun searchSubmitted(search: String) {
        filterList(search)
    }
    /**
     * нажатие на замечании
     */
    fun itemClicked(remark: RemarkFromCatalog) {
        chooseRemark(remark.id)
    }

    fun onScreenShown() {
        getData()
    }
    /**
     * новое замечание
     */
    fun onCustomRemarkClicked(text: String) {
        if (text.isEmpty()) {
            view.showEmptyNameError()
        } else {
            if (list.find { it.title == text } == null) {
                view.showCustomRemarkDialog(text)
            } else {
                view.showRemarkAlreadyAddedError()
            }
        }
    }

    fun setStartTab(startTab: Int) {
        this.startTab = startTab
    }
    /**
     * создать новое замечание
     */
    fun customRemarkCreate(text: String) {
        createCustomRemark(text)
    }
    /**
     * отменить новое замечание
     */
    fun customRemarkCancel() {
        view.dismissCustomRemarkDialog()
    }

    fun onResultReturned() {
        navigator.navigateBack()
    }
    /**
     * получить данные
     */
    private fun getData() {
        searchRemarkDisposable = remarkLoader.loadRemark()
                .subscribeOn(AppSchedulers.io())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.setLoadingVisible(false)
                            if (result.isSuccessful) {
                                list = result.remarks!!
                                view.updateAdapter(list)
                            } else {
                                /**
                                 * обработка ошибкит
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * создание нового замечания
     */
    private fun createCustomRemark(remarkName: String) {
        customRemarkDisposable.dispose()
        customRemarkDisposable = remarkWorker.createRemark(remarkName)
                .subscribeOn(AppSchedulers.singleThreadNetwork())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        navigator.navigateToCreateRemarkPhoto(result.remarkId!!, RemarkSearchActivity.START_PHOTO_REQUEST, startTab)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }

                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * выбор замечания
     */
    private fun chooseRemark(remarkId: String) {
        chooseRemarkDisposable.dispose()
        chooseRemarkDisposable = remarkWorker.addRemark(remarkId)
                .subscribeOn(AppSchedulers.singleThreadNetwork())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        navigator.navigateToCreateRemarkPhoto(result.remarkId!!, RemarkSearchActivity.START_PHOTO_REQUEST, startTab)
                    } else {
                        /**
                         * обработка ошибки
                         */
                        view.showError(result.userError.message)
                    }

                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }

    private fun filterList(text: String) {
        val filterList = list.filter { it.title.contains(text, true) }
        view.updateAdapter(filterList)
    }
}