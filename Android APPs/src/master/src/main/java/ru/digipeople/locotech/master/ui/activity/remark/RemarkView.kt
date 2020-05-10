package ru.digipeople.locotech.master.ui.activity.remark

import ru.digipeople.locotech.master.model.Remark
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс замечаний
 *
 * @author Kashonkov Nikita
 */
interface RemarkView : MvpView {
    /**
     * Установка данных в адаптер работ
     */
    fun setDataToWorkAdapter(list: List<Work>, shouldSavePosition: Boolean)
    /**
     * Установка данных в адаптер замечаний
     */
    fun setDataToRemarkAdapter(currentREmark: Remark, list: List<Remark>, shouldSavePosition: Boolean)
    /**
     * Управдление виджимостью информации
     */
    fun setDataVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
    /**
     * Установка прогресса выполнения
     */
    fun setEquipmentProgress(progress: Int, leftTime: Long, requiredTime: Long)
    /**
     * Отображение диалога удаления
     */
    fun showDeleteDialog()
    /**
     * Управление видимостью лоадера
     */
    fun setLoadingVisible(isVisible: Boolean)
}