package ru.digipeople.locotech.inspector.ui.activity.print.interactor

import io.reactivex.Single
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.api.model.DocumentSignerEntity
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.Signer
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.SignersCategoryModel
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Загрузчик категорий подписантов
 *
 * @author Aleksandr Brazhkin
 */
class SignersCategoriesLoader @Inject constructor(private val thingsWorxWorker: ThingsWorxWorker,
                                                  private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * загрузка категорий подписантов
     */
    fun loadSignersCategories(): Single<Result> {
        return thingsWorxWorker.documentsSigners()
                .map { Result(UserError.NO_ERROR, mapToSignersCategories(it.entityList)) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, emptyList())
                }
    }
    /**
     * перобразование данных
     */
    private fun mapToSignersCategories(entities: List<DocumentSignerEntity>) = entities.map {
        SignersCategoryModel().apply {
            id = it.id
            title = it.title
            it.signers.forEach {
                val signer = Signer()
                signer.id = it.id
                signer.name = it.name
                signers.add(signer)
            }
        }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val signersCatagories: List<SignersCategoryModel>) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}