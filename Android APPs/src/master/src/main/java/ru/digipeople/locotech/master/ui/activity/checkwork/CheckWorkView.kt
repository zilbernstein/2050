package ru.digipeople.locotech.master.ui.activity.checkwork

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
interface CheckWorkView: MvpView {
//    fun updateAdapter()
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * установка данных
     */
    fun setData(list: List<Work>)
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisible(isVisible: Boolean)
}