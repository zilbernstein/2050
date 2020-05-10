package ru.digipeople.photo.helper

import android.net.Uri

/**
 * Интерфейс для хранения фото
 *
 * @author Kashonkov Nikita
 */
interface PhotoFileStorage {
    /**
     * создание файла
     */
    fun createFile(): FileInfo
    /**
     * Провекрка пустого файла
     */
    fun checkFileIsEmpty(uri: Uri): Boolean
    /**
     * Создание директории
     */
    fun createDirectory()
    /**
     * Удаление файла
     */
    fun deleteFile(uri: Uri)
    /**
     * Удаление файлов
     */
    fun deleteFiles()
}