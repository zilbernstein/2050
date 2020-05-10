package ru.digipeople.locotech.master.ui.activity.workerspresence.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.ui.activity.auth.UserErrorBuilder
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.api.model.request.WorkerPresenceRequest
import ru.digipeople.locotech.master.model.WorkerPresence
import ru.digipeople.locotech.master.model.mapper.ChangeWorkerPresenceMapper
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.WorkerPresenceItem
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Класс изменяющий значение явки сотрудников
 */
class WorkerPresenceChangeInteractor @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val userErrorBuilder: UserErrorBuilder
) {
    /**
     * Создание маппера
     */
    private val mapper = ChangeWorkerPresenceMapper.INSTANCE
    /**
     * Изменение явки исполниетеля
     */
    fun changeWorkerPresence(workerPresenceItem: WorkerPresenceItem): Single<Result> = with(workerPresenceItem) {
        thingsWorxWorker
                .changeWorkerPresence(WorkerPresenceRequest(
                        id = id,
                        presence = presence,
                        workNight = night,
                        timeBegin = timeBegin?.time,
                        timeFinish = timeFinish?.time,
                        timeNight = timeNight?.time,
                        workTime = workTime?.time
                ))
                .map { result ->
                    /**
                     * Обработка результата
                     */
                    mapWorkerPresenceToItem(mapper.entityToModel(result)!!)
                }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn { throwable ->
                    Result(userError = userErrorBuilder.fromThowable(throwable))
                }
    }
    /**
     * Преобразование явки к элементу списка
     */
    private fun mapWorkerPresenceToItem(workerPresence: WorkerPresence): WorkerPresenceItem = with(workerPresence) {
        WorkerPresenceItem(id, fio, tabel, timeIn, timeOut, timeBegin, timeFinish, workTime, timeNight, presence, night)
    }
    /**
     * Стандартный ответ метода
     */
    class Result(val userError: UserError, private val _workerPresenceItem: WorkerPresenceItem? = null) {
        val workerPresenceItem
            get() = _workerPresenceItem!!
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}
