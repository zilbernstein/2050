package ru.digipeople.locotech.worker.ui.activity.mytask

import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.locotech.worker.model.Equipment

/**
 * Интерфейс структуры работы
 *
 * @author Kashonkov Nikita
 */
interface MyTaskView : MvpView {
    /**
     * загрузка данных в адаптер
     */
    fun setDataToAdapter(list: List<Equipment>)
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}