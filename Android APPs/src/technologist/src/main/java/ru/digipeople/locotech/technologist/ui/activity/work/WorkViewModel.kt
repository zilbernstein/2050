package ru.digipeople.locotech.technologist.ui.activity.work

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.technologist.model.Parameter
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.locotech.technologist.ui.activity.comment.CommentParams
import ru.digipeople.locotech.technologist.ui.activity.work.interactor.WorkLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewState работы
 */
class WorkViewModel @Inject constructor(private val workLoader: WorkLoader,
                                        private val navigator: Navigator) : BaseViewModel() {

    //region Params
    lateinit var workId: String
    //endregion
    //region LiveData
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val parametersLiveData = MutableLiveData<List<Parameter>>()
    val commentLiveData = MutableLiveData<String>()
    //endregion
    private var loadDataDisposable: Disposable = Disposables.disposed()

    override fun onStart() {
        noDataLiveData.value = true
    }

    fun onScreenShown() {
        loadData()
    }
    /**
     * Обработка нажатия на комментарий
     */
    fun onCommentClicked() {
        val commentParams = CommentParams(workId, commentLiveData.value ?: "")
        navigator.navigateToCommentActivity(commentParams)
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        loadDataDisposable.dispose()
        loadDataDisposable = workLoader.loadWorkDetails(workId)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    parametersLiveData.value = result.parameters
                    noDataLiveData.value = result.parameters.isEmpty()
                    commentLiveData.value = result.comment
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}