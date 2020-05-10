package ru.digipeople.message.ui.activity.messagedetail

import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import javax.inject.Inject

/**
 * ViewState деталки сообщения
 *
 * @author Kashonkov Nikita
 */
class MessageDetailViewState @Inject constructor(): BaseMvpViewState<MessageDetailView>(), MessageDetailView {
    override fun onViewAttached(view: MessageDetailView?) {}
    override fun onViewDetached(view: MessageDetailView?) {}
    /**
     * Переход назад
     */
    override fun navigateBack() { forEachView { it.navigateBack() }
    }
}