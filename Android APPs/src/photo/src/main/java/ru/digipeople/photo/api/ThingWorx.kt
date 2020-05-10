package ru.digipeople.photo.api

import io.reactivex.Single
import ru.digipeople.photo.api.model.response.PhotoResponse
import ru.digipeople.photo.api.model.response.ThingIdResponse
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.filetransfer.FileTransferResult
import ru.digipeople.thingworx.model.base.ApiResponse
import javax.inject.Inject

/**
 * Класс [ThingWorx] - Api модуля фото
 *
 * @author Kashonkov Nikita
 */
class ThingWorx @Inject constructor(private val baseThingWorx: BaseThingWorx) {
    /**
     * Метод для загрузки фотографий по замечанию
     *
     * @param remarkId - id замечания
     * @return [ApiResponse] cо списком фотографий
     */
    fun getRemarkPhotos(remarkId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map
        }.flatMap { requestThingWorx("photos_remark", it) }
    }

    /**
     * Метод для закрепления фотографий за замечанием
     *
     * @param remarkId - id замечания
     * @param idList - список id фотографий
     * @return [PhotoResponse] cо списком фотографий
     */
    fun clipPhotosToRemark(remarkId: String, idListJson: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map.put("id_list", idListJson)
            map
        }.flatMap { requestThingWorx("add_photos_remark", it) }
    }

    /**
     * Метод для удалении фотографии
     *
     * @param remarkId - id замечания
     * @param photoId - id фотографии
     * @return [PhotoResponse] cо списком фотографий
     */
    fun deletePhotoFromRemark(remarkId: String, photoId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_remark", remarkId)
            map.put("photo", photoId)
            map
        }.flatMap { requestThingWorx("delete_photo_remark", it) }
    }

    /**
     * Метод для загрузки фотографий по замечанию
     *
     * @param workId - id работы
     * @return [ApiResponse] cо списком фотографий
     */
    fun getWorkPhotos(workId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map
        }.flatMap { requestThingWorx("photos_work", it) }
    }

    /**
     * Метод для закрепления фотографий за замечанием
     *
     * @param workId - id работы
     * @param idList - список id фотографий
     * @return [PhotoResponse] cо списком фотографий
     */
    fun clipPhotosToWork(workId: String, idListJson: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map.put("id_list", idListJson)
            map
        }.flatMap { requestThingWorx("add_photos_work", it) }
    }

    /**
     * Метод для удалении фотографии
     *
     * @param workId - id работы
     * @param photoId - id фотографии
     * @return [PhotoResponse] cо списком фотографий
     */
    fun deletePhotoFromWork(workId: String, photoId: String): Single<ApiResponse> {
        return Single.fromCallable {
            val map = HashMap<String, String>()
            map.put("id_work", workId)
            map.put("photo", photoId)
            map
        }.flatMap { requestThingWorx("delete_photo_work", it) }
    }

    /**
     * Метод для загрузки фотографий по контрольно-сервисной операции(КСО)
     *
     * @param csoId - id работы
     * @return [ApiResponse] cо списком фотографий
     */
    fun getCsoPhotos(csoId: String) = requestThingWorx("photos_cso", mapOf("id_cso" to csoId))

    /**
     * Метод для закрепления фотографий за замечанием
     *
     * @param csoId - id работы
     * @param idList - список id фотографий
     * @return [PhotoResponse] cо списком фотографий
     */
    fun clipPhotosToCso(csoId: String, idListJson: String) = requestThingWorx("add_photos_cso", mapOf("id_cso" to csoId, "photos_list" to idListJson))

    /**
     * Метод для удалении фотографии
     *
     * @param csoId - id работы
     * @param photoId - id фотографии
     * @return [PhotoResponse] cо списком фотографий
     */
    fun deletePhotoFromCso(csoId: String, photoId: String) = requestThingWorx("delete_photo_cso", mapOf("id_cso" to csoId, "photo" to photoId))

    /**
     * Метод для отправки фотографий
     *
     * @param fileName - имя фотографии
     * @return [PhotoResponse] cо списком фотографий
     */
    fun sendPhoto(thingName: String, fileName: String): Single<FileTransferResult> {
        return baseThingWorx.sendFileToThingWorx(fileName, thingName)
    }

    /**
     * Метод для запроса идентификатора доступной финги
     *
     * @return [ThingIdResponse] c Id финги
     */
    fun requestLoaderThing() = requestThingWorx("free_thing", emptyMap())

    private fun requestThingWorx(serviceName: String, params: Map<String, Any>): Single<ApiResponse> {
        return baseThingWorx.requestThingWorxRx(serviceName, params)
    }
}