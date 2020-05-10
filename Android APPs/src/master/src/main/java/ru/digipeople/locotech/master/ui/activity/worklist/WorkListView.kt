package ru.digipeople.locotech.master.ui.activity.worklist

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс добавления замечания / работ
 *
 * @author Kashonkov Nikita
 */
interface WorkListView: MvpView {
    /**
     * Загрузка данных в адаптер
     */
    fun setDataToAdapter(list: List<Work>)
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisible(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}