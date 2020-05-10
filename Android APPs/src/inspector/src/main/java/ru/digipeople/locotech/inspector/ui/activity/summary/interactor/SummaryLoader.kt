package ru.digipeople.locotech.inspector.ui.activity.summary.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.SummaryEntity
import ru.digipeople.locotech.inspector.ui.activity.summary.adapter.SummaryItem
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Згрузчик сводной информации
 *
 * @author Sostavkin Grisha
 */
class SummaryLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка данных
     */
    fun loadSummary(): Single<Result> {
        return thingsWorxWorker.getSummary()
                .map { Result(UserError.NO_ERROR, mapToModels(it.entityList)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * обработка результата
     */
    private fun mapToModels(entities: List<SummaryEntity>): List<SummaryItem> {
        val items = arrayListOf<SummaryItem>()
        entities.forEach { summaryEntity ->
            val summaryItem = SummaryItem()
            summaryItem.accepted = summaryEntity.accepted
            summaryItem.declinedOTK = summaryEntity.declinedOtk
            summaryItem.declinedRJD = summaryEntity.declinedRzd
            summaryItem.name = summaryEntity.name
            val totalSum = summaryItem.accepted + summaryItem.declinedOTK + summaryItem.declinedRJD
            summaryItem.percent = if (totalSum != 0) {
                (summaryItem.accepted * 100) / totalSum
            } else {
                0
            }
            items.add(summaryItem)
        }
        return items
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val summaryItems: List<SummaryItem>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}