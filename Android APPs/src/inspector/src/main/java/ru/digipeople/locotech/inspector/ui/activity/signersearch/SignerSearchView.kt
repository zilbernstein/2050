package ru.digipeople.locotech.inspector.ui.activity.signersearch

import ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter.SignerModel
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Интерфейс поиска подписантов
 *
 * @author Kashonkov Nikita
 */
interface SignerSearchView : MvpView {
    /**
     * установка данных
     */
    fun setData(list: List<SignerModel>)
    /**
     * управлние видимостью лоадера
     */
    fun setLoadingVisibility(isVisible: Boolean)
    /**
     * обработка ошибки
     */
    fun showError(message: String)
}