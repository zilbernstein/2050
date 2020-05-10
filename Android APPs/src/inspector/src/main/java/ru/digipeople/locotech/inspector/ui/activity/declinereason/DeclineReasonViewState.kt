package ru.digipeople.locotech.inspector.ui.activity.declinereason

import ru.digipeople.locotech.inspector.ui.activity.declinereason.adapter.DeclinedReasonModel
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**Вью стейт причин удаления замечания
 *
 * @author Kashonkov Nikita
 */
class DeclineReasonViewState @Inject constructor() : BaseMvpViewState<DeclineReasonView>(), DeclineReasonView {

    override fun onViewAttached(view: DeclineReasonView) {}

    override fun onViewDetached(view: DeclineReasonView) {}
    /**
     * Установка данных
     */
    override fun setData(reasons: List<DeclinedReasonModel>) {
        forEachView { it.setData(reasons) }
    }
    /**
     * Управленеи видимостью лоадера
     */
    override fun showLoading(isVisible: Boolean) {
        forEachView { it.showLoading(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        forEachView { it.showError(error) }
    }
}