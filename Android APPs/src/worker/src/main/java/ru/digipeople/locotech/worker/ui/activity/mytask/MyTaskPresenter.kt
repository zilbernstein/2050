package ru.digipeople.locotech.worker.ui.activity.mytask

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.locotech.worker.data.task.CurrentTaskProvider
import ru.digipeople.locotech.worker.model.Work
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.mytask.interactor.WorkLoader
import javax.inject.Inject

/**
 * Презентер структуры работы
 *
 * @author Kashonkov Nikita
 */
class MyTaskPresenter @Inject constructor(viewState: MyTaskViewState,
                                          private val subscriptionProvider: SubscriptionProvider,
                                          private var taskProvider: CurrentTaskProvider,
                                          private val navigator: Navigator,
                                          private var workLoader: WorkLoader) :
        BaseMvpViewStatePresenter<MyTaskView, MyTaskViewState>(viewState) {
    //region Other
    private var loadWorkDisposable = Disposables.disposed()
    private var subscriptionDisposable = Disposables.disposed()
    //endRegion

    override fun onInitialize() {}

    fun onScreenShown() {
        getData()
        subscribeToEquipment()
    }
    /**
     * Обработка нажатия на работу
     */
    fun onItemClicked(work: Work) {
        navigator.navigateToWork(work.workId)
    }
    /**
     * Действи при уничтожении экрана
     */
    override fun destroy() {
        loadWorkDisposable.dispose()
        subscriptionDisposable.dispose()
        super.destroy()
    }
    /**
     * Получение данных
     */
    private fun getData() {
        loadWorkDisposable = workLoader.loadEquipments()
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.setDataToAdapter(result.equipments!!)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoadingVisibility(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * Подписка на оборудование
     */
    private fun subscribeToEquipment() {
        subscriptionDisposable.dispose()
        subscriptionDisposable = subscriptionProvider.workSubscription()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getData() }

    }


}