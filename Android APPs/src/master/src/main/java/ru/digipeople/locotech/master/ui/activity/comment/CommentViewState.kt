package ru.digipeople.locotech.master.ui.activity.comment

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View state комментариев
 *
 * @author Kashonkov Nikita
 */
class CommentViewState @Inject constructor() : BaseMvpViewState<CommentView>(), CommentView {
    override fun onViewAttached(view: CommentView?) {}

    override fun onViewDetached(view: CommentView?) {}
    /**
     * завершение активити
     */
    override fun finishActivity() {
        forEachView { it.finishActivity() }
    }
    /**
     * переход назад
     */
    override fun navigateBack() {
        forEachView { it.navigateBack() }
    }
    /**
     * установка текста комментария
     */
    override fun setText(text: String?) {
        forEachView { it.setText(text) }
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * переход к замеру
     */
    override fun navigateToMeasure(workId: String, workName: String, workStatus: Int) {
        forEachView { it.navigateToMeasure(workId, workName, workStatus) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoading(isVisible: Boolean) {
        forEachView { it.setLoading(isVisible) }
    }
}