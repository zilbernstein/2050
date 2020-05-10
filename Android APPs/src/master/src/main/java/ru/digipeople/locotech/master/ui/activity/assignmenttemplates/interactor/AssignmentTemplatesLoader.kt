package ru.digipeople.locotech.master.ui.activity.assignmenttemplates.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.ui.activity.auth.UserErrorBuilder
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.response.AssignmentTemplatesResponse
import ru.digipeople.locotech.master.model.AssignmentTemplate
import ru.digipeople.locotech.master.model.mapper.AssignmentTemplatesMapper
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter.item.AssignmentTemplateItem
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс выполняет загрузку шаблонов исполнителей
 */
class AssignmentTemplatesLoader @Inject constructor(
        private val thingWorxWorker: ThingsWorxWorker,
        private val errorBuilder: UserErrorBuilder
) {
    /**
     * Создание маппера
     */
    private val mapper = AssignmentTemplatesMapper.INSTANCE
    /**
     * Функция выполняет метод, возвращающий список шаблонов
     */
    fun loadAssignmentTemplates() = mapToResult(thingWorxWorker.assignmentTemplates())
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<AssignmentTemplatesResponse>): Single<Result> {
        return response
                .map { mapper.entityListToModelList(it.entityList) }
                .map { assignmentTemplates -> Result(UserError.NO_ERROR, mapToItems(assignmentTemplates)) }
                .onErrorReturn { Result(errorBuilder.fromThowable(it), emptyList()) }
    }
    /**
     * Преобразование к списку шаблонов
     */
    private fun mapToItems(assignmentTemplates: List<AssignmentTemplate>) = assignmentTemplates.map { AssignmentTemplateItem(it.id, it.name) }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val assignmentTemplates: List<AssignmentTemplateItem>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}