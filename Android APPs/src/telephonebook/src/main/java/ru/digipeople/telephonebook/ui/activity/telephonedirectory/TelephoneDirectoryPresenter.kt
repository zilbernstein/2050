package ru.digipeople.telephonebook.ui.activity.telephonedirectory

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.telephonebook.interactor.ContactsLoader
import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.telephonebook.ui.Navigator
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер для телефонного справочника
 *
 * @author Sostavkin Grisha
 */
class TelephoneDirectoryPresenter @Inject constructor(viewState: TelephoneDirectoryViewState,
                                                      private val contactsLoader: ContactsLoader,
                                                      private val navigator: Navigator)
    : BaseMvpViewStatePresenter<TelephoneDirectoryView, TelephoneDirectoryViewState>(viewState) {

    private var list: List<Contact> = emptyList()
    private var disposable: Disposable = Disposables.disposed()
    /**
     * инициализация презентера
     */
    override fun onInitialize() {
    }

    fun onScreenShow() {
        getData()
    }
    /**
     * действия при измение строки поиска
     */
    fun searchChanged(search: String) {
        filterList(search)
    }
    /**
     * начать поиск по строке
     */
    fun searchSubmitted(search: String) {
        filterList(search)
    }
    /**
     * обработка нажатия на звонок
     */
    fun callClicked(contact: Contact) {
        navigator.navigateToCall(contact.phoneNumber)
    }
    /**
     * получение списка контактов
     */
    private fun getData() {
        disposable.dispose()

        disposable = contactsLoader.loadContact()
                .doOnSuccess {result ->
                    /**
                     * обработк результата
                     */
                    if (result.isSuccessful){
                        list = result.contactList!!.sortedBy {
                            it.name
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            /**
                             * обработка результата
                             */
                            view.setLoading(false)
                            if (result.isSuccessful) {
                                view.setDataToAdapter(list)
                            } else {
                                view.showError(result.userError.message)
                            }
                        }, {
                    view.setLoading(false)
                    view.showError(it.message!!)
                }
                )
    }
    /**
     * фильтрация списка
     */
    private fun filterList(text: String) {
        val filterList = list.filter { it.name.contains(text, true) }
        view.setDataToAdapter(filterList)
    }
}