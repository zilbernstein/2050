package ru.digipeople.locotech.worker.ui.activity.task.interactor

import io.reactivex.Single
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс получающий данные по чеклисту
 *
 * @author Sostavkin Grisha
 */
class ChecklistChecker @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
 * Установка всех элементов в чеклисте
 */
    fun checkAllItemsChecked(workId: String): Single<Result> {
        return thingsWorxWorker.getChecklist(workId)
                .map { response ->
                    /**
                     * обработка результата
                     */
                    val hasUnchecked = response.items.find { !it.isChecked } != null
                    Result(UserError.NO_ERROR, !hasUnchecked)
                }
                .onErrorReturn {
                    /**
                     * обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, false)
                }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val allChecked: Boolean) {
        val isSuccessful
            get() = userError === UserError.NO_ERROR
    }
}