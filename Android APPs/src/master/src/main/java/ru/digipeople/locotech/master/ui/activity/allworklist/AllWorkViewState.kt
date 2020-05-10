package ru.digipeople.locotech.master.ui.activity.allworklist

import ru.digipeople.locotech.master.model.WorkFromCatalog
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * View State списка работ
 * @author Kashonkov Nikita
 */
class AllWorkViewState @Inject constructor() : BaseMvpViewState<AllWorkView>(), AllWorkView {
    private var isEmptySplashVisible = false

    override fun onViewAttached(view: AllWorkView) {
    }

    override fun onViewDetached(view: AllWorkView?) {}
    /**
     * Обновление данных в адаптере
     */
    override fun updateAdapter() {
        forEachView { it.updateAdapter() }
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setProgressVisibility(isVisible: Boolean) {
        forEachView { it.setProgressVisibility(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        forEachView { it.showError(error) }
    }
    /**
     * Загрузка данных в адаптер
     */
    override fun setDataToAdapter(list: List<WorkFromCatalog>) {
        forEachView { it.setDataToAdapter(list) }
    }
    /**
     * Вызов диалога запроса числа повторов
     */
    override fun setCountDialogVisibility(isVisible: Boolean) {
        forEachView { it.setCountDialogVisibility(isVisible) }
    }
    /**
     * Управление сообщением нет данных
     */
    override fun setEmptyDataSplashVisibility(isVisible: Boolean) {
        isEmptySplashVisible = isVisible
        forEachView { it.setEmptyDataSplashVisibility(isVisible) }
    }
}