package ru.digipeople.locotech.inspector.ui.activity.print.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.DocumentForPrintingEntity
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.DocumentModel
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик документов
 *
 * @author Aleksandr Brazhkin
 */
class DocumentsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                          private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка документов
     */
    fun loadDocuments(): Single<Result> {
        return thingsWorxWorker.documentsForPrinting()
                .map { Result(UserError.NO_ERROR, mapToDocuments(it.entityList)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * преобразование данных
     */
    private fun mapToDocuments(entities: List<DocumentForPrintingEntity>) = entities.map {
        DocumentModel().apply {
            id = it.id
            name = it.name
        }
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val documents: List<DocumentModel>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}