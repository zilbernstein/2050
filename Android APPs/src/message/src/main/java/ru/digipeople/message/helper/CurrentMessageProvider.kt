package ru.digipeople.message.helper

import io.reactivex.subjects.BehaviorSubject
import ru.digipeople.message.model.Message
import ru.digipeople.message.ui.fragment.messagedetail.MessageDetailPresenter
import ru.digipeople.utils.Ref
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс, предоставляющий данные для [MessageDetailPresenter].
 */
@Singleton
class CurrentMessageProvider @Inject constructor() {

    private val subject = BehaviorSubject.create<Ref<Message>>()

    val messageChanges: BehaviorSubject<Ref<Message>> = subject
    /**
     * Сообщение
     */
    var message: Message? = null
        set(value) {
            field = value
            subject.onNext(Ref(field))
        }
}