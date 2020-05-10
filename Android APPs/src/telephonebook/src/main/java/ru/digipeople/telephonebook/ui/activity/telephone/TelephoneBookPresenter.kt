package ru.digipeople.telephonebook.ui.activity.telephone

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.digipeople.telephonebook.interactor.ContactsLoader
import ru.digipeople.telephonebook.model.Contact
import ru.digipeople.telephonebook.ui.Navigator
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер телефонный справочник
 *
 * @author Sostavkin Grisha
 */
class TelephoneBookPresenter @Inject constructor(viewState: TelephoneBookViewState,
                                                 private val contactsLoader: ContactsLoader,
                                                 private val navigator: Navigator)
    : BaseMvpViewStatePresenter<TelephoneBookView, TelephoneBookViewState>(viewState) {
    private val permissionGrantedSubject = PublishSubject.create<Boolean>()

    private var list: List<Contact> = emptyList()
    private var disposable: Disposable = Disposables.disposed()
    private var permissionDisposable = Disposables.disposed()
    /**
     * инициализация презентера
     */
    override fun onInitialize() {
    }

    override fun destroy() {
        disposable.dispose()
        permissionDisposable.dispose()
        super.destroy()
    }

    fun onScreenShow() {
        getData()
    }
    /**
     * обработа изменения строки поиска
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
        permissionDisposable.dispose()
        permissionDisposable = permissionGrantedSubject.subscribe { granted ->
            if (granted) navigator.navigateToCall(contact.phoneNumber)
        }

        view.checkPermissions()
    }
    /**
     * обработка изменнеия разрешения
     */
    fun onPermissionChanged(granted: Boolean) {
        permissionGrantedSubject.onNext(granted)
    }
    /**
     * получение данных
     */
    private fun getData() {
        disposable = contactsLoader.loadContact()
                .doOnSuccess { result ->
                    /**
                     * обработка результата
                     */
                    if (result.isSuccessful) {
                        list = result.contactList!!.sortedBy {
                            it.name
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    view.setLoading(false)
                    if (result.isSuccessful) {
                        view.setDataToAdapter(list)
                    } else {
                        /**
                         * отображение ошибки
                         */
                        view.showError(result.userError.message)
                    }
                }, {
                    view.setLoading(false)
                    view.showError(it.message!!)
                })
    }
    /**
     * фильтрация контактов
     */
    private fun filterList(text: String) {
        if (text.isNotEmpty()) {
            val filterList = list.filter { it.name.contains(text, true) }
            view.setDataToAdapter(filterList)
        } else {
            view.setDataToAdapter(list)
        }
    }
}