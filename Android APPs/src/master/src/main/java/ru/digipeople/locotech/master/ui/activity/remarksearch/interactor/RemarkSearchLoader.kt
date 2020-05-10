package ru.digipeople.locotech.master.ui.activity.remarksearch.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.RemarkFromCatalog
import ru.digipeople.locotech.master.model.mapper.RemarkFromCatalogMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик добавления / создания замечания
 * 
 * @author Kashonkov Nikita
 */
class RemarkSearchLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = RemarkFromCatalogMapper.INSTANCE
    /**
     * Загрузка замечаний
     */
    fun loadRemark(): Single<Result> {
        return thingsWorxWorker.getRemarksFromCatalog()
                .map { mapper.entityListToModelList(it.entityList) }
                .map {
                    Result(UserError.NO_ERROR, it)
                }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val remarks: List<RemarkFromCatalog>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}