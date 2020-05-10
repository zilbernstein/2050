package ru.digipeople.message.ui.activity.chooseaddressee

import io.reactivex.Single
import io.reactivex.disposables.Disposables
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.message.helper.CreatingMessageProvider
import ru.digipeople.message.model.Contact
import ru.digipeople.message.ui.activity.chooseaddressee.interactor.ContactLoader
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import ru.digipeople.utils.AppSchedulers
import javax.inject.Inject

/**
 * Презентер структуры выбора адресата
 */
class ChooseAddresseePresenter @Inject constructor(viewState: ChooseAddresseeViewState,
                                                   private val contactLoader: ContactLoader,
                                                   private val creatingMessageProvider: CreatingMessageProvider) :
        BaseMvpViewStatePresenter<ChooseAddresseeView, ChooseAddresseeViewState>(viewState) {
    /**
     * Подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(ChooseAddresseePresenter::class)

    private var allContacts = emptyList<Contact>()
    private var selectedContacts = mutableMapOf<String, Contact>()
    private var loadContactsDisposable = Disposables.disposed()
    private var filterDisposable = Disposables.disposed()
    /**
     * Действия при инициализации
     */
    override fun onInitialize() {
        selectedContacts = HashMap(creatingMessageProvider.creatingMessage?.contacts
                ?: mutableMapOf())
        loadContacts()
    }

    override fun destroy() {
        super.destroy()
        loadContactsDisposable.dispose()
        filterDisposable.dispose()
    }
    /**
     * обработка изменения строки поиска
     */
    fun searchChanged(search: String) {
        filterContacts(search)
    }
    /**
     * нажатие на элемент списка
     */
    fun itemClicked(contact: Contact) {
        changeContactSelection(contact)
    }
    /**
     * Добавление адресата
     */
    fun addAddresseeClicked() {
        creatingMessageProvider.creatingMessage?.contacts = selectedContacts
        view.navigateBack()
    }
    /**
     * Загрузка контактов
     */
    private fun loadContacts() {
        loadContactsDisposable.dispose()
        loadContactsDisposable = contactLoader.loadContacts()
                .doOnSuccess { result ->
                    /**
                     * Обработка результата
                     */
                    result.contacts.forEach { contact ->
                        contact.isSelected = selectedContacts.containsKey(contact.id)
                    }
                }
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    view.setLoadingVisibility(false)
                    allContacts = result.contacts
                    view.setData(allContacts)
                    if (!result.isSuccessful) {
                        view.showUserError(result.userError)
                    }
                }, { logger.error(it) })
    }
    /**
     * изменение выбранных контактов
     */
    private fun changeContactSelection(contact: Contact) {
        if (contact.isSelected) {
            selectedContacts.remove(contact.id)
        } else {
            selectedContacts[contact.id] = contact
        }
        contact.isSelected = !contact.isSelected
    }
    /**
     * Фильтрация контактов
     */
    private fun filterContacts(query: String) {
        filterDisposable = Single
                .fromCallable { allContacts.filter { it.name.contains(query, true) } }
                .subscribeOn(AppSchedulers.computation())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { view.setLoadingVisibility(true) }
                .subscribe { contacts ->
                    view.setLoadingVisibility(false)
                    view.setData(contacts)
                }
    }
}