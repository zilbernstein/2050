package ru.digipeople.locotech.master.ui.activity.comment

import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс комментариев
 *
 * @author Kashonkov Nikita
 */
interface CommentView: MvpView {
    /**
     * переход назад
     */
    fun navigateBack()
    /**
     * завершение активити
     */
    fun finishActivity()
    /**
     * установка текста комментария
     */
    fun setText(text: String?)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * переход к замерам
     */
    fun navigateToMeasure(workId: String, workName: String, workStatus: Int)
    /**
     * управление видимостью лоадера
     */
    fun setLoading(isVisible: Boolean)
}