package ru.digipeople.locotech.master.ui.activity.urgent


import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.comment.CommentParams
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListParameters
import ru.digipeople.locotech.master.ui.activity.urgent.interactor.UrgentLoader
import ru.digipeople.locotech.master.ui.activity.urgent.interactor.WorkJob
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер срочно
 *
 * @author Kashonkov Nikita
 */
class UrgentPresenter @Inject constructor(urgentViewState: UrgentViewState,
                                          private val subscriptionProvider: SubscriptionProvider,
                                          private val urgentLoader: UrgentLoader,
                                          private val workJob: WorkJob,
                                          private val navigator: Navigator) : BaseMvpViewStatePresenter<UrgentView, UrgentViewState>(urgentViewState) {

    private var loaderDisposable = Disposables.disposed()
    private var compositeDisposable = CompositeDisposable()
    private var subscriptionDisposable = Disposables.disposed()
    private var returnedWork: Work? = null
    /**
     * Действия при нициализации презентера
     */
    override fun onInitialize() {}

    fun onScreenShown() {
        getData()
        subscribeToUrgent()
    }
    /**
     * Показ комментариев
     */
    fun showComment(work: Work) {
        val commentParams = CommentParams(work.id, work.title, work.comment?.text)
        navigator.navigateToWorkComment(commentParams)
    }
    /**
     * нгачать работу
     */
    fun startWorkClicked(work: Work) {
        startWork(work)
    }
    /**
     * остановить работу
     */
    fun stopWorkClicked(work: Work) {
        stopWork(work)
    }
    /**
     * Вернуть работу
     */
    fun returnWorkClicked(work: Work) {
        returnWork(work)
    }
    /**
     * Принять работу
     */
    fun acceptWorkClicked(work: Work) {
        acceptWork(work)
    }
    /**
     * Добавление комментария к возвразенной работе
     */
    fun onCommentAddedToReturnedWork() {
        startWork(returnedWork!!)
    }
    /**
     * Комментарий об ошибке при возврате
     */
    fun onCommentAddedToReturnWorkMistake() {
        returnedWork = null
    }
    /**
     * Нажатие на ТМЦ
     */
    fun onTmcClicked(work: Work) {
        navigator.navigateToTmcList(TmcListParameters(work.id, work.title))
    }
    /**
     * Нажатие на Замеры
     */
    fun onMeasurementClicked(work: Work){
        navigator.navigateToMeasurement(work.id,work.title, work.status.code)
    }

    override fun destroy() {
        compositeDisposable.dispose()
        loaderDisposable.dispose()
        subscriptionDisposable.dispose()
        super.destroy()
    }
    /**
     * Получение данных
     */
    private fun getData() {
        loaderDisposable.dispose()
        loaderDisposable = urgentLoader.loadUrgent()
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoadingVisible(false)
                    if (result.isSuccessful) {
                        view.setDataToAdapter(result.works!!)
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
    /**
     * Старт работы
     */
    private fun startWork(work: Work) {
        compositeDisposable.add(workJob.startWork(work.id)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            if (result.isSuccessful) {
                                getData()
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.showError(it.message!!)
                })
        )
    }
    /**
     * Остановка работы
     */
    private fun stopWork(work: Work) {
        compositeDisposable.add(workJob.stopWork(work.id)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * Обработка результата
                             */
                            if (result.isSuccessful) {
                                getData()
                            } else {
                                /**
                                 * отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.showError(it.message!!)
                })
        )
    }
    /**
     * Принять работу
     */
    private fun acceptWork(work: Work) {
        compositeDisposable.add(workJob.acceptWork(work.id)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            if (result.isSuccessful) {
                                getData()
                            } else {
                                /**
                                 * Отображение ошибки
                                 */
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.showError(it.message!!)
                })
        )
    }
    /**
     * возврат ошибки
     */
    private fun returnWork(work: Work) {
        returnedWork = work
        showCommentBeforeReturn(work)
    }
    /**
     * пока комментария перед возвратом работы
     */
    private fun showCommentBeforeReturn(work: Work) {
        val commentParams = CommentParams(work.id, work.title, work.comment?.text)
        navigator.navigateToEditCommentForResult(commentParams)
    }

    private fun subscribeToUrgent() {
        subscriptionDisposable.dispose()
        subscriptionDisposable = subscriptionProvider.urgentAmountSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getData() }

    }

}