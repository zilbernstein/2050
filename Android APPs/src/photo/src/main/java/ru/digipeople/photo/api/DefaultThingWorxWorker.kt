package ru.digipeople.photo.api

import io.reactivex.Single
import ru.digipeople.photo.api.model.response.PhotoResponse
import ru.digipeople.photo.api.model.response.ThingIdResponse
import ru.digipeople.thingworx.filetransfer.FileTransferResult
import ru.digipeople.thingworx.helper.JsonConverter
import ru.digipeople.thingworx.helper.ResponseConverter
import ru.digipeople.thingworx.model.IDListEntity
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Класс [DefaultThingWorxWorker] - реализация интерфейса [ThingWorxWorker]
 *
 * @author Kashonkov Nikita
 */
class DefaultThingWorxWorker(private val thingWorx: ThingWorx,
                             private val responseConverter: ResponseConverter) : ThingWorxWorker {
    /**
     * Метод для загрузки фотографий по замечанию
     */
    override fun getRemarkPhotos(remarkId: String): Single<PhotoResponse> {
        return thingWorx.getRemarkPhotos(remarkId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, PhotoResponse::class.java)
                }
    }
    /**
     * Метод для отправки фотографий
     */
    override fun sendPhoto(fileName: String): Single<FileTransferResult> = requestLoaderThing()
            .flatMap { result -> thingWorx.sendPhoto(result.name, fileName) }

    /**
     * Метод для закрепления фотографий за замечанием
     */
    override fun clipPhotosToRemark(remarkId: String, idList: List<String>): Single<ResultResponse> {
        return Single.fromCallable {
            val entity = IDListEntity(idList)
            JsonConverter.convertToJson(entity)
        }
                .flatMap { thingWorx.clipPhotosToRemark(remarkId, it) }
                .map { response ->
                    responseConverter.convertResultEntity(response.result)
                }
    }
    /**
     * Метод для удалении фотографии
     */
    override fun deletePhotoFromRemark(remarkId: String, photoId: String): Single<ResultResponse> {
        return thingWorx.deletePhotoFromRemark(remarkId, photoId)
                .map { response ->
                    responseConverter.convertResultResponse(response.result)
                }
    }
    /**
     * Метод для загрузки фотографий по замечанию
     */
    override fun getWorkPhotos(workId: String): Single<PhotoResponse> {
        return thingWorx.getWorkPhotos(workId)
                .map { response ->
                    responseConverter.convertToEntityList(response.result, PhotoResponse::class.java)
                }
    }
    /**
     * Метод для закрепления фотографий за замечанием
     */
    override fun clipPhotosToWork(workId: String, idList: List<String>): Single<ResultResponse> = Single
            .fromCallable {
                val entity = IDListEntity(idList)
                JsonConverter.convertToJson(entity)
            }
            .flatMap { thingWorx.clipPhotosToWork(workId, it) }
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для удалении фотографии
     */
    override fun deletePhotoFromWork(workId: String, photoId: String): Single<ResultResponse> {
        return thingWorx.deletePhotoFromWork(workId, photoId)
                .map { response ->
                    responseConverter.convertResultResponse(response.result)
                }
    }
    /**
     * Метод для удалении фотографии
     */
    override fun deletePhotoFromCso(workId: String, photoId: String): Single<ResultResponse> = thingWorx.deletePhotoFromCso(workId, photoId)
            .map { response ->
                responseConverter.convertResultResponse(response.result)
            }
    /**
     * Метод для загрузки фотографий по контрольно-сервисной операции(КСО)
     */
    override fun getCsoPhotos(csoId: String): Single<PhotoResponse> = thingWorx.getCsoPhotos(csoId)
            .map { response ->
                responseConverter.convertToEntityList(response.result, PhotoResponse::class.java)
            }

    /**
     * Метод для закрепления фотографий за замечанием
     */
    override fun clipPhotosToCso(csoId: String, idList: List<String>) = Single
            .fromCallable {
                val entity = IDListEntity(idList)
                JsonConverter.convertToJson(entity)
            }
            .flatMap { thingWorx.clipPhotosToCso(csoId, it) }
            .map { response ->
                responseConverter.convertResultEntity(response.result)
            }
    /**
     * Метод для запроса идентификатора доступной финги
     */
    override fun requestLoaderThing(): Single<ThingIdResponse> = thingWorx.requestLoaderThing()
            .map { response ->
                responseConverter.convertToEntity(response.result, ThingIdResponse::class.java)
            }
}