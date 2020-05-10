package ru.digipeople.locotech.master.ui.activity.remarksearch

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.master.model.RemarkFromCatalog
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.remarksearch.interactor.RemarkSearchLoader
import ru.digipeople.locotech.master.ui.activity.remarksearch.interactor.RemarkSearchWorker
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер добавления / создания замечания
 *
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
    //endRegion
    /**
     * Действия при инициализации презентера
     */
    override fun onInitialize() {}
    /**
     * Изменени данных в строке поиска
     */
    fun searchChanged(search: String) {
        filterList(search)
    }
    /**
     * Принять поиск по строке
     */
    fun searchSubmitted(search: String) {
        filterList(search)
    }
    /**
     * Отрабока нажатия на элемент
     */
    fun itemClicked(remark: RemarkFromCatalog) {
        chooseRemark(remark.id)
    }

    fun onScreenShown() {
        getData()
    }
    /**
     * Отработка нажатия на создание кастомного замечания
     */
    fun customRemarkClicked(text: String) {
        if (text.isEmpty()) {
            view.showEmptyNameError()
        } else {
            view.showCustomRemarkDialog(text)
        }
    }
    /**
     * Создание кастомного замечания
     */
    fun customRemarkCreate(text: String) {
        createCustomRemark(text)
    }
    /**
     * Отмена создания замечания
     */
    fun customRemarkCancel() {
        view.dismissCustomRemarkDialog()
    }
    /**
     * Получение списка замечаний
     */
    private fun getData() {
        searchRemarkDisposable = remarkLoader.loadRemark()
                .subscribeOn(AppSchedulers.io())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        list = result.remarks!!
                        view.updateAdapter(list)
                    } else {
                        view.showError(result.userError.message)
                        /**
                         * Отображение ошибки
                         */
                    }
                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Сощжание нового замечания
     */
    private fun createCustomRemark(remarkName: String) {
        customRemarkDisposable.dispose()
        customRemarkDisposable = remarkWorker.createRemark(remarkName)
                .subscribeOn(AppSchedulers.singleThreadNetwork())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        navigator.navigateToAddWorkFromCustomRemark(result.remarkId!!)
                    } else {
                        /**
                         * сообщение об ошибке
                         */
                        view.showError(result.userError.message)
                    }

                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Выбор замечания
     */
    private fun chooseRemark(remarkId: String) {
        chooseRemarkDisposable.dispose()
        chooseRemarkDisposable = remarkWorker.choiceRemark(remarkId)
                .subscribeOn(AppSchedulers.singleThreadNetwork())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        navigator.navigateToWorkList(result.remarkId!!)
                    } else {
                        /**
                         * сообщение об ошибке
                         */
                        view.showError(result.userError.message)
                    }

                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Фильтрация списка
     */
    private fun filterList(text: String) {
        val filterList = list.filter { it.title.contains(text, true) }
        view.updateAdapter(filterList)
    }


}