package ru.digipeople.message.ui.activity.messagedetail

import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер деталки сообщения
 *
 * @author Kashonkov Nikita
 */
class MessageDetailPresenter @Inject constructor(viewState: MessageDetailViewState): BaseMvpViewStatePresenter<MessageDetailView, MessageDetailViewState>(viewState) {
    override fun onInitialize() {}
    /**
     * Обработка перехода на предыдущий экран
     */
    fun onBackButtonClicked(){view.navigateBack()}
}