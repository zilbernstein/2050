package ru.digipeople.locotech.metrologist.ui.activity.sections.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.model.Section
import ru.digipeople.locotech.metrologist.helper.session.SessionManager
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс осуществляющий выбор секции
 *
 * @author Michael Solenov
 */
class SelectSectionInteractor @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                                  private val errorBuilder: SimpleApiUserErrorBuilder,
                                                  private val sessionManager: SessionManager) {
    /**
     * Выбор секции
     */
    fun selectSection(section: Section): Single<Result> {
        return thingsWorxWorker.postSelectSection(section.id)
                .andThen(Single.just(Result(UserError.NO_ERROR)))
                .doOnSuccess {
                    /**
                     * Обработка результата
                     */
                    sessionManager.setCurrentSection(section.id, section.number + " " + section.subNumber)
                }
                .onErrorReturn {
                    /**
                     * Обработка ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}