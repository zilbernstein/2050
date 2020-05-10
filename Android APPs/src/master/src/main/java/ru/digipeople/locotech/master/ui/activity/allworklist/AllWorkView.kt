package ru.digipeople.locotech.master.ui.activity.allworklist

import ru.digipeople.locotech.master.model.WorkFromCatalog
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Инттерфейс списка работ
 *
 * @author Kashonkov Nikita
 */
interface AllWorkView : MvpView {
    /**
     * Обновление данных в адаптере
     */
    fun updateAdapter()
    /**
     * Загрузка данных в адаптер
     */
    fun setDataToAdapter(list: List<WorkFromCatalog>)
    /**
     * Управление видимостью лоадера
     */
    fun setProgressVisibility(isVisible: Boolean)
    /**
     * Вызов диалога запроса числа повторов
     */
    fun setCountDialogVisibility(isVisible: Boolean)
    /**
     * Управление сообщением нет данных
     */
    fun setEmptyDataSplashVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(error: UserError)
}