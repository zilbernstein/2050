package ru.digipeople.locotech.worker.ui.activity.tmcshortage

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.TMCInWork
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.comment.CommentParams
import ru.digipeople.locotech.worker.ui.activity.tmcshortage.interactor.TmcLoader
import javax.inject.Inject

/**
 * Презентер сттруктуры недостающие ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcShortagePresenter @Inject constructor(viewState: TmcShortageViewState,
                                               private val navigator: Navigator,
                                               private val workId: String,
                                               private val context: Context,
                                               private val tmcLoader: TmcLoader) : BaseMvpViewStatePresenter<TmcShortageView, TmcShortageViewState>(viewState) {
    private lateinit var allTmcList: MutableList<TMCInWork>
    private lateinit var currentList: MutableList<TMCInWork>
    private var loadTMCDisposable = Disposables.disposed()
    /**
     * Действия при инициализации презентера
     */
    override fun onInitialize() {
        currentList = mutableListOf()
    }

    fun onScreenShown() {
        getData()
        currentList = mutableListOf()
    }
    /**
     * Обработка нажатия назад
     */
    fun onBackIconPressed() {
        navigator.navigateBack()
    }
    /**
     * Обработка нажатия принять
     */
    fun onCheckedPressed(commentParams: CommentParams) {
        if (currentList.isEmpty()) {
            view.showCheckMistake()
        } else {
            val stringBuilder = StringBuilder()
            stringBuilder.append(commentParams.text)
            stringBuilder.append("\n")
            stringBuilder.append(context.getString(R.string.tmc_shortage_prefix))

            currentList.forEach {
                stringBuilder.append(" ${it.name},")
            }
            stringBuilder.replace(stringBuilder.length - 1, stringBuilder.length, "")

            val comment = CommentParams(workId, stringBuilder.toString())
            navigator.navigateToComment(comment, TmcShortageActivity.START_COMMENT_REQUEST)
        }
    }
    /**
     * Обработкеа нажатия на элемент списка
     */
    fun onItemClicked(tmc: TMCInWork) {
        if (currentList.contains(tmc)) {
            currentList.remove(tmc)
        } else {
            currentList.add(tmc)
        }
        view.updateAdapter()
    }

    fun resultReturned(isSuccessful: Boolean) {
        if (isSuccessful) {
            view.finishActivityWithResult()
        }
    }
    /**
     * Получение данных
     */
    private fun getData() {
        loadTMCDisposable.dispose()
        loadTMCDisposable = tmcLoader.loadTMC(workId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    if (result.isSuccessful) {
                        view.setDataToAdapter(result.tmc!!, currentList)
                    } else {
                        /**
                         * Отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, { throwable: Throwable ->
                    view.setLoadingVisibility(false)
                    view.showError(throwable.message!!)
                })

    }
}