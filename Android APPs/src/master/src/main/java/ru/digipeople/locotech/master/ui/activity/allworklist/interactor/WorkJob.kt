package ru.digipeople.locotech.master.ui.activity.allworklist.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выбора работы
 *
 * @author Kashonkov Nikita
 */
class WorkJob @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                  private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Функция добавления работы в замечание
     */
    fun addWork(remarkId: String, workId: String, repeatsCount: Int): Single<Result> {
        return mapToResult(thingsWorxWorker.addWorks(remarkId, workId, repeatsCount))
    }

    /**
     * Функция преобразования ответа к требуемому виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * Базовый класс ответа метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}