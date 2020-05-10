package ru.digipeople.photo.ui.activity.photogallery.interactor

import io.reactivex.Single
import ru.digipeople.photo.api.ThingWorxWorker
import ru.digipeople.photo.model.mapper.InteractionResultMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.thingworx.model.response.ResultResponse
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Класс для работы с фотографиями через платформу ThingWorz
 *
 * @author Kashonkov Nikita
 */
class PhotoWorker @Inject constructor(private val thingsWorxWorker: ThingWorxWorker,
                                      private val errorBuilder: SimpleApiUserErrorBuilder) {
    private val mapper = InteractionResultMapper.INSTANCE
    /**
     * Прикрепление фото к замечанию
     */
    fun clipPhotosToRemark(remarkId: String, idList: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.clipPhotosToRemark(remarkId, idList))
    }
    /**
     * Удаление фото в замечании
     */
    fun deletePhotoFromRemark(remarkId: String, photoId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.deletePhotoFromRemark(remarkId, photoId))
    }
    /**
     * Прикрепление фото к работе
     */
    fun clipPhotosToWork(workId: String, idList: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.clipPhotosToWork(workId, idList))
    }
    /**
     * Удаление фото из работы
     */
    fun deletePhotoFromWork(workId: String, photoId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.deletePhotoFromWork(workId, photoId))
    }
    /**
     * Прикрепление фото
     */
    fun clipPhotosToCso(csoId: String, idList: List<String>): Single<Result> {
        return mapToResult(thingsWorxWorker.clipPhotosToCso(csoId, idList))
    }
    /**
     * Удаление фото
     */
    fun deletePhotoFromCso(csoId: String, photoId: String): Single<Result> {
        return mapToResult(thingsWorxWorker.deletePhotoFromCso(csoId, photoId))
    }
    /**
     * Преобразование данных к требуемому виду
     */
    private fun mapToResult(response: Single<ResultResponse>): Single<Result> {
        return response.map { Result(UserError.NO_ERROR) }
                .onErrorReturn {
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