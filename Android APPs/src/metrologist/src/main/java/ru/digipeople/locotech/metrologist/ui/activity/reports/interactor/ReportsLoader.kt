package ru.digipeople.locotech.metrologist.ui.activity.reports.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.reportMapper
import ru.digipeople.locotech.metrologist.data.model.Report
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик отчетов
 */
class ReportsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка списка отчетов
     */
    fun load(): Single<Result> {
        return thingsWorxWorker.getReports()
                .map { reportMapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }

    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val reports: List<Report>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}