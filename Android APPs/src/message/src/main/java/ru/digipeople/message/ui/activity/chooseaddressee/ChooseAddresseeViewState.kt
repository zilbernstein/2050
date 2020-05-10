package ru.digipeople.message.ui.activity.chooseaddressee

import ru.digipeople.message.model.Contact
import ru.digipeople.ui.mvp.viewState.BaseMvpViewState
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * ViewState для структуры выбора адресата
 */
class ChooseAddresseeViewState @Inject constructor() : BaseMvpViewState<ChooseAddresseeView>(), ChooseAddresseeView {
    override fun onViewAttached(view: ChooseAddresseeView?) {}

    override fun onViewDetached(view: ChooseAddresseeView?) {}

    override fun setData(contacts: List<Contact>) {
        forEachView { it.setData(contacts) }
    }

    override fun navigateBack() {
        forEachView { it.navigateBack() }
    }

    override fun setLoadingVisibility(isVisible: Boolean) {
        forEachView { it.setLoadingVisibility(isVisible) }
    }

    override fun showUserError(userError: UserError) {
        forEachView { it.showUserError(userError) }
    }
}