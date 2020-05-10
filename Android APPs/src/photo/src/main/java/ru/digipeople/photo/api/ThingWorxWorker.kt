package ru.digipeople.photo.api

import io.reactivex.Single
import ru.digipeople.photo.api.model.response.PhotoResponse
import ru.digipeople.photo.api.model.response.ThingIdResponse
import ru.digipeople.thingworx.filetransfer.FileTransferResult
import ru.digipeople.thingworx.model.response.ResultResponse

/**
 * Интерфейс для работы с Апи
 *
 * @author Kashonkov Nikita
 */
interface ThingWorxWorker {
    /**
     * Метод для загрузки фотографий по замечанию
     *
     * @param remarkId - id замечания
     * @return [PhotoResponse] cо списком фотографий
     */
    fun getRemarkPhotos(remarkId: String): Single<PhotoResponse>

    /**
     * Метод для отправки фотографий
     *
     * @param photoPath - путь к файлу
     * @param fileName - имя файла
     * @return [PhotoResponse] cо списком фотографий
     */
    fun sendPhoto(fileName: String): Single<FileTransferResult>

    /**
     * Метод для закрепления фотографий за замечанием
     *
     * @param remarkId - id замечания
     * @param idList - список id фотографий
     * @return [PhotoResponse] cо списком фотографий
     */
    fun clipPhotosToRemark(remarkId: String, idList: List<String>): Single<ResultResponse>

    /**
     * Метод для удалении фотографии
     *
     * @param remarkId - id замечания
     * @param photoId - id фотографии
     * @return [PhotoResponse] cо списком фотографий
     */
    fun deletePhotoFromRemark(remarkId: String, photoId: String): Single<ResultResponse>

    /**
     * Метод для загрузки фотографий по замечанию
     *
     * @param workId - id работы
     * @return [PhotoResponse] cо списком фотографий
     */
    fun getWorkPhotos(workId: String): Single<PhotoResponse>

    /**
     * Метод для закрепления фотографий за замечанием
     *
     * @param workId - id замечания
     * @param idList - список id фотографий
     * @return [PhotoResponse] cо списком фотографий
     */
    fun clipPhotosToWork(workId: String, idList: List<String>): Single<ResultResponse>

    /**
     * Метод для удалении фотографии
     *
     * @param workId - id замечания
     * @param photoId - id фотографии
     * @return [PhotoResponse] cо списком фотографий
     */
    fun deletePhotoFromCso(workId: String, photoId: String): Single<ResultResponse>

    /**
     * Метод для загрузки фотографий по замечанию
     *
     * @param csoId - id работы
     * @return [PhotoResponse] cо списком фотографий
     */
    fun getCsoPhotos(csoId: String): Single<PhotoResponse>

    /**
     * Метод для закрепления фотографий за замечанием
     *
     * @param csoId - id замечания
     * @param idList - список id фотографий
     * @return [PhotoResponse] cо списком фотографий
     */
    fun clipPhotosToCso(csoId: String, idList: List<String>): Single<ResultResponse>

    /**
     * Метод для удалении фотографии
     *
     * @param csoId - id замечания
     * @param photoId - id фотографии
     * @return [PhotoResponse] cо спискок фотографий
     */
    fun deletePhotoFromWork(csoId: String, photoId: String): Single<ResultResponse>

    /**
     * Метод для запроса идентификатора доступной финги
     *
     * @return [ThingIdResponse] c Id финги
     */
    fun requestLoaderThing(): Single<ThingIdResponse>
}