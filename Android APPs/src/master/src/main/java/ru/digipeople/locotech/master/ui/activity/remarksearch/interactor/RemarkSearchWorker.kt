package ru.digipeople.locotech.master.ui.activity.remarksearch.interactor

import ru.digipeople.locotech.master.api.ThingsWorxWorker
import io.reactivex.Single
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.locotech.master.api.model.response.CreateRemarkResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс выполняющий методы выбора и создание нового замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkSearchWorker @Inject constructor(private val thingWorxWorker: ThingsWorxWorker,
                                             private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Выбор замечания
     */
    fun choiceRemark(remarkId: String): Single<Result>{
        return mapToResult(thingWorxWorker.chooseRemark(remarkId))
    }
    /**
     * Создание замечания
     */
    fun createRemark(remarkName: String): Single<Result> {
        return mapToResult(thingWorxWorker.createRemark(remarkName))
    }
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<CreateRemarkResponse>): Single<Result> {
        return response.map {
            Result(UserError.NO_ERROR, it.remarkId)
        }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }

    }
    /**
     * Стандартный ответ методов
     */
    data class Result(val userError: UserError, val remarkId: String?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}