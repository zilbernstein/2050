package ru.digipeople.locotech.master.ui.activity.workerspresence.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.ui.activity.auth.UserErrorBuilder
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.response.BrigadePresenceResponse
import ru.digipeople.locotech.master.model.BrigadePresence
import ru.digipeople.locotech.master.model.WorkerPresence
import ru.digipeople.locotech.master.model.mapper.BrigadePresenceMapper
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.BrigadePresenceItem
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.WorkerPresenceItem
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик явки сотрудников
 */
class BrigadesPresenceLoader @Inject constructor(
        private val thingWorx: ThingsWorxWorker,
        private val errorBuilder: UserErrorBuilder
) {
    /**
     * Создание маппера
     */
    private val mapper = BrigadePresenceMapper.INSTANCE

    fun loadPresence() = mapToResult(thingWorx.brigadePresence())
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<BrigadePresenceResponse>): Single<Result> {
        return response
                .map { mapper.entityListToModelList(it.entityList) }
                .map { brigadePresence -> Result(UserError.NO_ERROR, mapToItems(brigadePresence)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Привязка данных к формату бригад и исполнителей
     */
    private fun mapToItems(brigadeList: List<BrigadePresence>): List<Any> {
        val items = mutableListOf<Any>()
        brigadeList.forEach { brigade ->
            items.add(mapBrigadeToItem(brigade))
            brigade.workerPresenceList.forEach { workerPresence ->
                items.add(mapWorkerPresenceToItem(workerPresence))
            }
        }
        return items
    }
    /**
     * Преобразование Бригады к типу элемента списка
     */
    private fun mapBrigadeToItem(brigade: BrigadePresence): BrigadePresenceItem {
        return BrigadePresenceItem(brigade.id, brigade.name)
    }
    /**
     * Преобразование исполнителя к типу элемента списка
     */
    private fun mapWorkerPresenceToItem(workerPresence: WorkerPresence): WorkerPresenceItem = with(workerPresence) {
        WorkerPresenceItem(id, fio, tabel, timeIn, timeOut, timeBegin, timeFinish, workTime, timeNight, presence, night)
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, val brigadesItems: List<Any>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}