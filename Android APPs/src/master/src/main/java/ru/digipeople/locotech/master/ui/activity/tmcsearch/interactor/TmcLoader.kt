package ru.digipeople.locotech.master.ui.activity.tmcsearch.interactor

import io.reactivex.Single
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.model.TMC
import ru.digipeople.locotech.master.model.mapper.TMCMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик поиска ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                    private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Создание маппера
     */
    private val mapper = TMCMapper.INSTANCE
    /**
     * Загрузка списка ТМЦ
     */
    fun loadTmcList(queryStr: String, tmcIdSet: Set<String>, stockAvailable: Boolean): Single<Result> {
        return thingsWorxWorker.getTMC(queryStr, stockAvailable)
                .map { mapper.entityListToModelList(it.entityList) }
                .map {
                    val newList = it.filter { tmc ->
                        //Не отображаются те элементы, которые уже добавлены в список данной работы.
                        // Фильтр "Есть на складе".
                        !tmcIdSet.contains(tmc.id) && if (stockAvailable) tmc.stockRest > 0.0 else tmc.stockRest == 0.0 && tmc.workshop == 0.0
                    }
                    return@map Result(UserError.NO_ERROR, newList)
                }
                .onErrorReturn {
                    /**
                     * Отображение ошибки
                     */
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val tmcList: List<TMC>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}