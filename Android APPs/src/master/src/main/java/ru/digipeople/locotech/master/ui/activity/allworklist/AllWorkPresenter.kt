package ru.digipeople.locotech.master.ui.activity.allworklist

import ru.digipeople.locotech.master.model.WorkFromCatalog
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.allworklist.interactor.WorkJob
import ru.digipeople.locotech.master.ui.activity.allworklist.interactor.WorkLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер списка работ
 *
 * @author Kashonkov Nikita
 */
/**
 * Конструктор класса
 */
class AllWorkPresenter @Inject constructor(viewState: AllWorkViewState,
                                           private val workLoader: WorkLoader,
                                           private val workJob: WorkJob,
                                           private val navigator: Navigator,
                                           private val callingType: Int,
                                           private val remarkId: String) : BaseMvpViewStatePresenter<AllWorkView, AllWorkViewState>(viewState) {
    //region other

    private val logger = LoggerFactory.getLogger(AllWorkPresenter::class)

    private lateinit var selectedWork: WorkFromCatalog
    private var queryStr = ""
    private var loadingWorkDisposable = Disposables.disposed()
    private var addWorkDisposable = Disposables.disposed()
    //endregion

    override fun onInitialize() {}
    /**
     * Действия, выполняющиеся при уничтожении
     */
    override fun destroy() {
        loadingWorkDisposable.dispose()
    }
    /**
     * Действия, при изменении строки запроса
     */
    fun onQueryStrChanged(queryStr: String) {
        this.queryStr = queryStr
        loadData(queryStr)
    }
    /**
     * Действия при нажатиии на элемент списка
     */
    fun itemClicked(work: WorkFromCatalog) {
        selectedWork = work
        view.setCountDialogVisibility(true)
    }
    /**
     * Действия при выборе числа повторов
     */
    fun onRepeatsCountChoose(count: Int) {
        addWork(count)
    }

    fun onScreenShown() {
        loadData(queryStr)
    }
    /**
     * Действия при нажатии назад
     */
    fun backButtonClicked() {
        navigator.navigateBack()
    }
    /**
     * Загрузка данных
     */
    private fun loadData(queryStr: String) {
        loadingWorkDisposable.dispose()
        /**
         * Метод выполняется только при длине запроса больше 3 символов
         */
        if (queryStr.length < 3){
            view.setProgressVisibility(false)
            view.setDataToAdapter(emptyList())
            view.setEmptyDataSplashVisibility(false)
            return
        }
        loadingWorkDisposable = workLoader.loadWork(remarkId, queryStr)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.setProgressVisibility(true)}
                .subscribe(
                        { result ->
                            view.setProgressVisibility(false)
                            view.setDataToAdapter(result.works)
                            view.setEmptyDataSplashVisibility(result.works.isEmpty())
                            if (!result.isSuccessful) {
                                view.showError(result.userError)
                            }
                        },
                        { logger.error(it) })
    }
    /**
     * Добавление работы
     */
    private fun addWork(repeatsCount: Int) {
        addWorkDisposable = workJob.addWork(remarkId, selectedWork.id, repeatsCount)
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setProgressVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view.setProgressVisibility(false)
                    if (result.isSuccessful) {
                        /**
                         * Если на экран перешли с экрана заметок, то переход на него, иначе возврат на предыдущий
                         */
                        if (callingType == AllWorkActivity.CALLING_CUSTOM_REMARK) {
                            navigator.navigateToWorkList(remarkId)
                        } else {
                            navigator.navigateBack()
                        }
                    } else {
                        view.setProgressVisibility(false)
                        view.showError(result.userError)
                    }
                }, { logger.error(it) })
    }
}