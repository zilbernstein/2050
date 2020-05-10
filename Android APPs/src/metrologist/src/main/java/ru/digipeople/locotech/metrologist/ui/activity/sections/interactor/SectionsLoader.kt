package ru.digipeople.locotech.metrologist.ui.activity.sections.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.model.Locomotive
import ru.digipeople.locotech.metrologist.data.api.mapper.LocomotiveMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик секций
 */
class SectionsLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                         private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    val mapper = LocomotiveMapper.INSTANCE
    /**
     * Загрузка локомотивов и секций
     */
    fun loadLocoSections(): Single<Result> {
        return thingsWorxWorker.getSections()
                .map { mapper.entityListToModelList(it.entityList) }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val locomotives: List<Locomotive>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}