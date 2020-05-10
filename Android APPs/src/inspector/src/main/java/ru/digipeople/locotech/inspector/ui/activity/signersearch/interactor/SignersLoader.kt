package ru.digipeople.locotech.inspector.ui.activity.signersearch.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.SignerEntity
import ru.digipeople.locotech.inspector.model.Signer
import ru.digipeople.locotech.inspector.model.mapper.SignerItemMapper
import ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter.SignerModel
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 *Загрузчик поиска подписантов
 *
 * @author Kashonkov Nikita
 */
class SignersLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * создание маппера
     */
    val mapper = SignerItemMapper.INSTANCE
    /**
     * загрузка исполнителей
     */
    fun loadSigners(name: String): Single<Result> {
        return thingsWorxWorker.getExecutiveList(name)
                .map {
                    Result(UserError.NO_ERROR, mapToModels(it.entityList))
                }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * обработка результата
     */
    private fun mapToModels(entities: List<SignerEntity>) = entities.map {
        SignerModel(Signer().apply {
            name = it.fio
            id = it.id
            post = it.post
        })
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val signers: List<SignerModel>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }

}