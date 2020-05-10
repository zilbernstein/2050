package ru.digipeople.locotech.master.ui.activity.divide

import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State разделения работы
 *
 * @author Kashonkov Nikita
 */
class DivideViewState @Inject constructor(): BaseMvpViewState<DivideView>(), DivideView {
    private var work: Work? = null

    override fun onViewAttached(view: DivideView) {
        view.showModel(work)
    }

    override fun onViewDetached(view: DivideView?) {}
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * упарвление видимостью ладера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
    /**
     * установка времени
     */
    override fun setTime(time: Long) {
        forEachView { it.setTime(time) }
    }
    /**
     * установка процентов
     */
    override fun setPercent(percent: String) {
        forEachView { it.setPercent(percent) }
    }
    /**
     * установка процентов после изменения времени
     */
    override fun setPercentAfterTimeChanged(percent: String) {
        forEachView { it.setPercentAfterTimeChanged(percent) }
    }
    /**
     * отображение параметров разделения работы
     */
    override fun showModel(work: Work?) {
        this.work = work
        forEachView { it.showModel(work) }
    }
    /**
     * управление доступностью кнопки разделить
     */
    override fun setDivideButtonEnabled(isEnable: Boolean) {
        forEachView { it.setDivideButtonEnabled(isEnable) }
    }
    /**
     * очистка времени
     */
    override fun hideDividedTime() {
        forEachView { it.hideDividedTime() }
    }
    /**
     * очистка процентов
     */
    override fun hidePercent() {
        forEachView { it.hidePercent() }
    }
    /**
     * отображение диалога выбора времени
     */
    override fun showTimePickerDialog(initTIme: Long, maxTime: Long) {
        forEachView { it.showTimePickerDialog(initTIme, maxTime) }
    }

}