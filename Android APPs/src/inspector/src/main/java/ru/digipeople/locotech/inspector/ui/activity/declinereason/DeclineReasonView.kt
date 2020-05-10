package ru.digipeople.locotech.inspector.ui.activity.declinereason

import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonModel
import ru.digipeople.ui.mvp.view.MvpView
import ru.digipeople.utils.model.UserError

/**
 * Интерфейс причин удаления замечания
 *
 * @author Kashonkov Nikita
 */
interface DeclineReasonView: MvpView {
    /**
     * Установка данных
     */
    fun setData(reasons: List<DeclinedReasonModel>)
    /**
     * Управлекние видимостью загрузки
     */
    fun showLoading(isVisible: Boolean)
    /**
     * Отображение ошибки
     */
    fun showError(error: UserError)
}