package ru.digipeople.locotech.master.ui.activity.approved

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.master.interactor.SetWorkaroundInteractor
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.approved.interactor.ApproveWorkLoader
import ru.digipeople.locotech.master.ui.activity.comment.CommentParams
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListParameters
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер согласование
 *
 * @author Kashonkov Nikita
 */
class ApprovedPresenter @Inject constructor(
        approvedViewState: ApprovedViewState,
        private val subscriptionProvider: SubscriptionProvider,
        private val navigator: Navigator,
        private val workLoader: ApproveWorkLoader,
        private val setWorkaroundInteractor: SetWorkaroundInteractor
) : BaseMvpViewStatePresenter<ApprovedView, ApprovedViewState>(approvedViewState) {
    //region Other
    private var unapprovedWorkList = emptyList<Work>()
    private var approvedWorkList = emptyList<Work>()
    private var loadWorkDisposable = Disposables.disposed()
    private var subscriptionDisposable = Disposables.disposed()
    private var workStatusDisposable = Disposables.disposed()
    //endRegion
    /**
     * Инициализация презентера
     */
    override fun onInitialize() {
    }
    /**
     * Операции при уничтожении презентера
     */
    override fun destroy() {
        loadWorkDisposable.dispose()
        subscriptionDisposable.dispose()
        workStatusDisposable.dispose()
        super.destroy()
    }

    fun onScreenShown() {
        loadWorks()
        subscribeToApproval()
    }
    /**
     * Обработка нажатия на комментарий
     */
    fun onCommentClicked(work: Work) {
        navigator.navigateToWorkComment(CommentParams(work.id, work.title, work.comment?.text))
    }
    /**
     * Обработка нажатия на кнопку согласовать
     */
    fun approvedButtonClicked() {
        navigator.navigateToCheckWorkBeforeApprove()
    }
    /**
     * Обработка нажатия на пункт меню ТМЦ
     */
    fun onTmcClicked(work: Work) {
        navigator.navigateToTmcList(TmcListParameters(work.id, work.title))
    }
    /**
     * Обработка нажатия на пункт меню обходное решение
     */
    fun onWorkaroundClicked(work: Work) {
        if (WorkStatus.lessThen(work.status, WorkStatus.IN_TASK)) {
            workStatusDisposable = setWorkaroundInteractor.setWorkaround(work.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.isSuccessful) {
                            loadWorks()
                        } else {
                            view.showError(result.userError.message)
                        }
                    }, {
                        view.showError(it.message!!)
                    })
        }
    }
    /**
     * Загрузка списка работ
     */
    fun loadWorks() {
        loadWorkDisposable = workLoader.loadWork()
                .doOnSuccess { result ->
                    if (result.isSuccessful) {
                        /**
                         * разделение на согласовано и не согласовано
                         */
                        unapprovedWorkList = result.works!!.filter {
                            it.status == WorkStatus.NOT_APPROVED
                        }
                        approvedWorkList = result.works.filter {
                            it.status == WorkStatus.IN_APPROVE
                                    || it.status == WorkStatus.APPROVED
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisible(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            view.setLoadingVisible(false)
                            if (result.isSuccessful) {
                                /**
                                 * Передача данных в адаптер
                                 */
                                view.setDataToApprovedAdapter(approvedWorkList)
                                view.setDataToNorApprovedAdapter(unapprovedWorkList)
                            } else {
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoadingVisible(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Подписка на согласование
     */
    fun subscribeToApproval() {
        subscriptionDisposable.dispose()
        subscriptionDisposable = subscriptionProvider.approvalSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadWorks() }
    }
}