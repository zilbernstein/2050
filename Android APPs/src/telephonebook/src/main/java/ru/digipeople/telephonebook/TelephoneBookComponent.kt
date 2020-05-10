package ru.digipeople.telephonebook

import ru.digipeople.telephonebook.ui.activity.telephone.TelephoneBookScreenComponent
import ru.digipeople.telephonebook.ui.activity.telephonedirectory.TelephoneDirectoryScreenComponent

/**
 * Компонент модуля telephonebook
 *
 * @author Sostavkin Grisha
 */
interface TelephoneBookComponent {

    fun telephoneBookDetailsScreenComponent(): TelephoneBookScreenComponent

    fun telephoneDirectoryScreenComponent(): TelephoneDirectoryScreenComponent

    companion object {
        private lateinit var instance: TelephoneBookComponent

        fun get(): TelephoneBookComponent = instance

        fun set(telephoneBookComponent: TelephoneBookComponent) {
            instance = telephoneBookComponent
        }
    }
}