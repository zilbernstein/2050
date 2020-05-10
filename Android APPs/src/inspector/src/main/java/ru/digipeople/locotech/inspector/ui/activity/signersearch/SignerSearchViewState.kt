package ru.digipeople.locotech.inspector.ui.activity.signersearch


import ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter.SignerModel
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * View State поиска подписантов
 *
 * @author Kashonkov Nikita
 */
class SignerSearchViewState @Inject constructor() : BaseMvpViewState<SignerSearchView>(), SignerSearchView {
    override fun onViewAttached(view: SignerSearchView?) {}

    override fun onViewDetached(view: SignerSearchView?) {}
    /**
     * установка данных
     */
    override fun setData(list: List<SignerModel>) {
        forEachView { it.setData(list) }
    }
    /**
     * отображние ошибки
     */
    override fun showError(message: String) {
        forEachView { it.showError(message) }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }
}