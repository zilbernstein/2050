package ru.digipeople.photo.ui.activity.photogallery.interactor

import io.reactivex.Single
import ru.digipeople.photo.api.ThingWorxWorker
import ru.digipeople.thingworx.filetransfer.FileTransferResult
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик фотографий на сервер
 *
 * @author Kashonkov Nikita
 */
class PhotoUploader @Inject constructor(private val thingWorxWorker: ThingWorxWorker,
                                        private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Загрузка фото
     */
    fun uploadPhoto(fileName: String): Single<Result> {
        return mapToResult(thingWorxWorker.sendPhoto(fileName))
    }

    private fun mapToResult(response: Single<FileTransferResult>): Single<Result> {
        return response.map { transferResult ->
            Result(UserError.NO_ERROR)
        }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError)
                }
    }

    data class Result(val userError: UserError) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}