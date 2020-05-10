package ru.digipeople.locotech.worker.ui.activity.checklist.interactor

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.model.ChecklistItem
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс реализующий логику отправки проставления/снятия чекбокса у чеклиста
 *
 * @author Sostavkin Grisha
 */
class ChecklistToggleInteractor @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * обработка чеклиста
     */
    fun toggleChecklistItem(workId: String, checklistItem: ChecklistItem): Single<Result> {
        return Single
                .defer {
                    /**
                     * разные методы в зависимости о того ставится галочка или снимается
                     */
                    if (checklistItem.isChecked) {
                        thingsWorxWorker.postUncheckChecklistItem(workId, checklistItem.id)
                    } else {
                        thingsWorxWorker.postCheckChecklistItem(workId, checklistItem.id)
                    }
                }
                .map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError === UserError.NO_ERROR
    }
}