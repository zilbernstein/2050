package ru.digipeople.photo.ui.activity.photogallery.interactor

import io.reactivex.Single
import ru.digipeople.photo.api.ThingWorxWorker
import ru.digipeople.photo.model.Photo
import ru.digipeople.photo.model.mapper.PhotoMapper
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Загрузчик фотографий
 *
 * @author Kashonkov Nikita
 */
class PhotoLoader @Inject constructor(private val thingWorxWorker: ThingWorxWorker,
                                      private val errorBuilder: SimpleApiUserErrorBuilder) {
    /**
     * Сощдание маппера
     */
    private val mapper = PhotoMapper.INSTANCE
    /**
     * Загрузка фото из замечания
     */
    fun loadRemarkPhotos(id: String): Single<Result> {
        return thingWorxWorker.getRemarkPhotos(id)
                .map { response ->
                    /**
                     * обработка ответа
                     */
                    mapper.entityListToModelList(response.entityList)
                }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Загрузка фото в работе
     */
    fun loadWorkPhotos(id: String): Single<Result> {
        return thingWorxWorker.getWorkPhotos(id)
                .map { response ->
                    /**
                     * Обработка ответа
                     */
                    mapper.entityListToModelList(response.entityList)
                }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Загрузка фото из
     */
    fun loadCsoPhotos(id: String): Single<Result>{
        return thingWorxWorker.getCsoPhotos(id)
                .map { response ->
                    /**
                     * обработка ответа
                     */
                    mapper.entityListToModelList(response.entityList)
                }
                .map { Result(UserError.NO_ERROR, it) }
                .onErrorReturn {
                    val userError = errorBuilder.fromThowable(it)
                    Result(userError, null)
                }
    }
    /**
     * Стандартный ответ метода
     */
    data class Result(val userError: UserError, val photos: List<Photo>?) {
        val isSuccessful
            get() = userError == UserError.NO_ERROR
    }
}