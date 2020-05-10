package ru.digipeople.locotech.master.ui.activity.allworklist.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.WorkFromCatalog
import ru.digipeople.locotech.master.model.mapper.WorkFromCatalogMapper
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик списка работ
 *
 * @author Kashonkov Nikita
 */
class WorkLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                     private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = WorkFromCatalogMapper.INSTANCE
    /**
     * Функция загрузки списка работ
     */
    fun loadWork(remarkId: String, queryStr: String): Single<Result> {
        return thingsWorxWorker.getWorksFromCatalog(remarkId, queryStr)
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Базовый класс ответа метода
     */
    data class Result(val userError: UserError, val works: List<WorkFromCatalog>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}