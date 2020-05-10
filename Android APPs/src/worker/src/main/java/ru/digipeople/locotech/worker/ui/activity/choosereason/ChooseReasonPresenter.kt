package ru.digipeople.locotech.worker.ui.activity.choosereason

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.locotech.worker.model.PauseReason
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.choosereason.interactor.ReasonLoader
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * Презентер выбора причины остановки работы
 *
 * @author Kashonkov Nikita
 */
class ChooseReasonPresenter @Inject constructor(viewState: ChooseReasonViewState,
                                                private val workId: String,
                                                private val reasonLoader: ReasonLoader,
                                                private val navigator: Navigator) :
        BaseMvpViewStatePresenter<ChooseReasonView, ChooseReasonViewState>(viewState) {

    //region Other
    var loadDisposable = Disposables.disposed()
    private lateinit var reasonId: String

    private lateinit var commentParam: CommentParams
    //endRegion
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {}

    fun onScreenShown(commentParams: CommentParams) {
        getData()
        commentParam = commentParams
    }
    /**
     * Обработка нажатия назад
     */
    fun onBackIconPressed() {
        navigator.navigateBack()
    }
    /**
     * Обработка нажатия на элемента списка
     */
    fun onItemClicked(pauseReason: PauseReason) {
        reasonId = pauseReason.id
        // Временный хардкод для показа
        if (pauseReason.reasonName.equals("отсутствие тмц", true)) {
            navigator.navigateToChooseTmc(workId, ChooseReasonActivity.START_TMC_SHORTAGE_REQUEST, commentParam)
        } else {
            val stringBuilder = StringBuilder()
            stringBuilder.append(commentParam.text)
            stringBuilder.append("\n")
            stringBuilder.append(pauseReason.reasonName)
            val comment = CommentParams(workId, stringBuilder.toString())
            navigator.navigateToComment(comment, ChooseReasonActivity.START_COMMENT_REQUEST)
        }
    }

    fun resultReturned(isSuccessful: Boolean) {
        if (isSuccessful) {
            view.finishActivityWithResult(reasonId)
        }
    }
    /**
     * Получение данных
     */
    private fun getData() {
        loadDisposable = reasonLoader.loadReasons()
                .subscribeOn(AppSchedulers.network())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.setData(result.reasons!!)
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


}