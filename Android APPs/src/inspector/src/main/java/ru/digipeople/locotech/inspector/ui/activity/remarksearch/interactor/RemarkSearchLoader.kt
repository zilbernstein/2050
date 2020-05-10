package ru.digipeople.locotech.inspector.ui.activity.remarksearch.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.model.RemarkFromCatalog
import ru.digipeople.locotech.inspector.model.mapper.RemarkFromCatalogMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик добавления/выбора замечаний
 *
 * @author Kashonkov Nikita
 */
class RemarkSearchLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * содание маппера
     */
    val mapper = RemarkFromCatalogMapper.INSTANCE
    /**
     * загрузить замечания
     */
    fun loadRemark(): Single<Result> {
        return thingsWorxWorker.getRemarksFromCatalog()
                .map { result ->
                    /**
                     * обработка результата
                     */
                    mapper.entityListToModelList(result.entityList)
                }
                .map {
                    Result(UserError.NO_ERROR, it)
                }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * стандартный ответ метода
     */
    data class Result(val userError: UserError, val remarks: List<RemarkFromCatalog>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}