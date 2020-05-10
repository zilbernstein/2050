package ru.digipeople.locotech.worker.ui.activity.choosereason

import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.locotech.worker.model.PauseReason

/**
 * @author Kashonkov Nikita
 */
interface ChooseReasonView : MvpView {
    /**
     * Установка данных
     */
    fun setData(list: List<PauseReason>)
    /**
     * Завершение активити с возвратом результата
     */
    fun finishActivityWithResult(reasonId: String)
    /**
     * Управление видимостью ладера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(message: String)
}