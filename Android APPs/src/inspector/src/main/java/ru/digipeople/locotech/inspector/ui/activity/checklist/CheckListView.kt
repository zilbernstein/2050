package ru.digipeople.locotech.inspector.ui.activity.checklist

import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.OperationData
import ru.digipeople.ui.mvp.view.MvpView

/**
 * интерфейс чек-листа
 * @author Kashonkov Nikita
 */
interface CheckListView : MvpView {
    /**
     * установка заголовка
     */
    fun setTitle(equipmentName: String?)
    /**
     * установка данных
     */
    fun setData(adapterData: AdapterData?)
    /**
     * обновить работу
     */
    fun updateWork(operationData: OperationData, position: Int)
    /**
     * показать диалог согласовния
     */
    fun showApproveDialog()
    /**
     * отмена согласования
     */
    fun dismissApproveDialog()
    /**
     * управление видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение ошибки
     */
    fun showError(message: String)
    /**
     * управление видимостью кнопки завершить
     */
    fun setFinishBtnVisibility(isVisibile: Boolean)
}