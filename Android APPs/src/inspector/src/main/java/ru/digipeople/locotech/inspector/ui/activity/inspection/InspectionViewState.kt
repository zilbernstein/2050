package ru.digipeople.locotech.inspector.ui.activity.inspection

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject
/**
 * Экранный компонент инспекционного контроля
 *
 * @author Kashonkov Nikita
 */
class InspectionViewState @Inject constructor() : BaseMvpViewState<InspectionView>(), InspectionView {
    private var equipmentTitle: String? = null
    private var isProgressVisible = false

    override fun onViewAttached(view: InspectionView) {
        view.setTitle(equipmentTitle)
        view.setProgressVisibility(isProgressVisible)
    }

    override fun onViewDetached(view: InspectionView) {}
    /**
     * Установка заголовка
     */
    override fun setTitle(equipmentTitle: String?) {
        this.equipmentTitle = equipmentTitle
        forEachView { it.setTitle(equipmentTitle) }
    }
    /**
     * Установка видимости лоадера
     */
    override fun setProgressVisibility(isVisible: Boolean) {
        isProgressVisible = isVisible
        forEachView { it.setProgressVisibility(isVisible) }
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * Сообщение пользователю
     */
    override fun showSuccessfullyNotifiedMessage() {
        forEachView { it.showSuccessfullyNotifiedMessage() }
    }
}