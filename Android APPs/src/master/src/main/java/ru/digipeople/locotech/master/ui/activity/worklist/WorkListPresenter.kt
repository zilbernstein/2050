package ru.digipeople.locotech.master.ui.activity.worklist

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerParams
import ru.digipeople.locotech.master.ui.activity.worklist.interactor.RemarkWorker
import ru.digipeople.locotech.master.ui.activity.worklist.interactor.WorkLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер добавления замечания / работ
 *
 * @author Kashonkov Nikita
 */
class WorkListPresenter @Inject constructor(viewState: WorkListViewState,
                                            private val remarkId: String,
                                            private val navigator: Navigator,
                                            private val workLoader: WorkLoader,
                                            private val remarkWorker: RemarkWorker) :
        BaseMvpViewStatePresenter<WorkListView, WorkListViewState>(viewState) {
    //region other
    private var loadDisposeble = Disposables.disposed()
    private var createRemarkDisposable = Disposables.disposed()
    //endregion
    /**
     * Действия при нициализации
     */
    override fun onInitialize() {}
    /**
     * Обработка нажатия назад
     */
    fun backButtonClicked() {
        navigator.navigateBack()
    }
    /**
     * Обработка добавления замечания
     */
    fun addRemarkButtonClicked() {
        createRemark()
    }

    fun onScreenShown() {
        getData()
    }
    /**
     * Получение данных
     */
    private fun getData() {
        loadDisposeble = workLoader.loadWork(remarkId)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обрадотка результата
                     */
                    view.setLoadingVisible(false)
                    if(result.isSuccessful){
                        view.setDataToAdapter(result.works!!)
                    } else {
                        /**
                         * Отобраэение ошибки
                         */
                        view.showError(result.userError.message)
                    }

                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Создание замечания
     */
    private fun createRemark(){
        createRemarkDisposable = remarkWorker.addRemark(remarkId)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe{ view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisible(false)
                    if(result.isSuccessful){
                        navigator.navigateToRemark()
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }

                }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })

    }
}