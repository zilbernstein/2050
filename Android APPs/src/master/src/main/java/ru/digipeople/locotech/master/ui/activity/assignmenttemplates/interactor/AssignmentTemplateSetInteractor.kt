package ru.digipeople.locotech.master.ui.activity.assignmenttemplates.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.ui.activity.auth.UserErrorBuilder
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter.item.AssignmentTemplateItem
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import java.util.*
import javax.inject.Inject
/**
 * Класс выполняет метод выбора шаблона
 */
class AssignmentTemplateSetInteractor @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: UserErrorBuilder
) {
    fun setAssignmentTemplate(item: AssignmentTemplateItem, date: Date, nightShift: Boolean): Single<Result> =
            mapToResponse(thingsWorxWorker.setAssignmentTemplate(item.id, date.time, nightShift))
    /**
     * Преобразование ответа к требуемому виду
     */
    private fun mapToResponse(response: Single<ResultResponse>) = response
            .map { Result(UserError.NO_ERROR) }
            .onErrorReturn { throwable -> Result(errorBuilder.fromThowable(throwable)) }

    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}