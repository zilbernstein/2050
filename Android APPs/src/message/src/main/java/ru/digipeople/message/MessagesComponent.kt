package ru.digipeople.message

import ru.digipeople.message.api.ThingsWorxWorker
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.activity.chooseaddressee.ChooseAddresseeScreenComponent
import ru.digipeople.message.ui.activity.message.MessageScreenComponent
import ru.digipeople.message.ui.activity.messagedetail.MessageDetailScreenComponent
import ru.digipeople.message.ui.activity.messagelist.MessageListScreenComponent
import ru.digipeople.message.ui.activity.writemessage.WriteMessageScreenComponent

/**
 * Интерфейс модуля message
 */
interface MessagesComponent {
    /**
     * Компонент экрана сообщения
     */
    fun messageScreenComponent(): MessageScreenComponent
    /**
     * Компонент экрана структуры выбора адресата
     */
    fun chooseAddresseeScreenComponent(): ChooseAddresseeScreenComponent
    /**
     * Компонент экрана списка сообщений
     */
    fun messageListScreenComponent(): MessageListScreenComponent
    /**
     * Компонент экрана деталки сообщения
     */
    fun messageDetailScreenComponent(): MessageDetailScreenComponent
    /**
     * Компонент экрана отправки сообщения
     */
    fun writeMessageScreenComponent(): WriteMessageScreenComponent
    /**
     * Активити навигатор для модуля message
     */
    fun messageNavigator(): MessageActivityNavigator
    /**
     * Интерфейс для работы с апи
     */
    fun thingWorxWorker(): ThingsWorxWorker

    companion object {
        private lateinit var instance: MessagesComponent

        fun get(): MessagesComponent = instance

        fun set(profileComponent: MessagesComponent) {
            instance = profileComponent
        }
    }
}