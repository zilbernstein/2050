package ru.digipeople.locotech.metrologist.ui.activity.reports.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс отправки отчета
 * @author Michael Solenov
 */
class ReportSender @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                       private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Отправка отчета
     */
    fun send(reportId: String, emailAddress: String): Single<UserError> {
        return thingsWorxWorker.taskSendReport(reportId, emailAddress)
                .andThen(Single.just(UserError.NO_ERROR))
                .onErrorReturn { errorBuilder.fromThowable(it) }
    }
}