package ru.digipeople.locotech.master.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Классс выполняет метод установки обходного решения
 */
class SetWorkaroundInteractor @Inject constructor(
        private val thingsWorxWorker: ThingsWorxWorker,
        private val errorBuilder: SimpleApiUserErrorBuilder
) {
    /**
     * Функция установки обходного решения
     */
    fun setWorkaround(workId: String): Single<Result> {
        /**
         * Метод для первода работы в статус "Обходное решение" (WORKAROUND)
         */
        return thingsWorxWorker.workaround(workId)
                .map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }

    class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}