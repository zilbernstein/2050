package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.interactor

import io.reactivex.Single
import ru.digipeople.locotech.metrologist.data.api.ThingsWorxWorker
import ru.digipeople.locotech.metrologist.data.api.mapper.measurementCategoryMapper
import ru.digipeople.locotech.metrologist.data.model.MeasurementCategory
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

class CategoriesLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                           private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка типов замеров
     */
    fun load(): Single<Result> {
        return thingsWorxWorker.getMeasurementCategories()
                .map { Result(UserError.NO_ERROR, measurementCategoryMapper.fromEntityList(it.entityList)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError,
                      val categories: List<MeasurementCategory>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}
