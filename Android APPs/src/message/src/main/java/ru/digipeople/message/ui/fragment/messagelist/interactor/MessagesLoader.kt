package ru.digipeople.message.ui.fragment.messagelist.interactor

import ru.digipeople.message.api.ThingsWorxWorker
import ru.digipeople.message.model.Message
import ru.digipeople.message.model.mapper.messageMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загружает сообщения
 *
 * @author Kashonkov Nikita
 */
class MessagesLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                         private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * получить сообщения
     */
    fun getMessages() = thingsWorxWorker.getMessages()
            .map { messageMapper.fromEntityList(it.entityList) }
            .map { messages ->
                /**
                 * обработка сообщения
                 */
                val sortedMessages = messages.sortedByDescending { it.date }
                val incomeMessages = mutableListOf<Message>()
                val outcomeMessages = mutableListOf<Message>()
                sortedMessages.forEach {
                    if (it.isIncoming) {
                        incomeMessages.add(it)
                    } else {
                        outcomeMessages.add(it)
                    }
                }
                Result(UserError.NO_ERROR, incomeMessages, outcomeMessages)
            }
            .onErrorReturn {
                /**
                 * обработка ошибки
                 */
                val userError = errorBuilder.fromThowable(it)
                Result(userError, emptyList(), emptyList())
            }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError,
                 val incomeMessages: List<Message>,
                 val outcomeMessages: List<Message>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}