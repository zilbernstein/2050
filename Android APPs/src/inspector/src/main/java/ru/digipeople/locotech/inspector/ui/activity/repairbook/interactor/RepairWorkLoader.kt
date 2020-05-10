package ru.digipeople.locotech.inspector.ui.activity.repairbook.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.Document
import ru.digipeople.locotech.inspector.model.mapper.DocumentsMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик книги ремонтов
 */
class RepairWorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                           private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание маппера
     */
    val mapper = DocumentsMapper.INSTANCE
    /**
     * получение документов
     */
    fun getDocuments(): Single<Result> {
        return thingsWorxWorker.getDocuments()
                .map { Result(UserError.NO_ERROR, mapper.entityListToModelList(it.entityList)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * стандартный ответ метода
     */
    class Result(val userError: UserError, val documents: List<Document>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}