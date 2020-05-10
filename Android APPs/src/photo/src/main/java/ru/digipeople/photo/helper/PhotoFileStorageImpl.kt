package ru.digipeople.photo.helper

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Реализация хранения фото
 *
 * @author Kashonkov Nikita
 */
class PhotoFileStorageImpl constructor(private val context: Context) : PhotoFileStorage {

    private var directory: File? = null
    /**
     * Создание файла
     */
    override fun createFile(): FileInfo {
        if (directory == null) {
            createDirectory()
        }

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val path = StringBuilder()
                .append("JPEG_")
                .append(timeStamp)
                .append(".jpg")
                .toString()

        val file = File(directory, path)
        file.createNewFile()

        return FileInfo(getUriFromFile(file), path)
    }
    /**
     * Проверка что файло пуст
     */
    override fun checkFileIsEmpty(uri: Uri): Boolean {
        val file = File(uri.path)
        return file.length() == 0L
    }
    /**
     * Удаление файла
     */
    override fun deleteFile(uri: Uri) {
        val file = File(uri.path)
        if (file.exists()) {
            file.delete()
        }
    }
    /**
     * Удаление файлов
     */
    override fun deleteFiles() {
        if (directory == null) {
            createDirectory()
        }

        if (directory!!.exists()) {
            directory!!.listFiles()?.forEach {
                it.delete()
            }
//            directory!!.delete()
        }

//        directory = null
    }
    /**
     * Создание директории
     */
    override fun createDirectory() {
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        directory = storageDir

        directory = File(storageDir, PHOTO_DIRECTORY)
        directory!!.mkdir()
    }
    /**
     * полученеи Uri файла
     */
    private fun getUriFromFile(file: File): Uri {
        val pack = context.packageName
        return FileProvider.getUriForFile(
                context,
                "$pack.fileprovider",
                file
        )
    }

    companion object {
        private const val PHOTO_DIRECTORY = "Locotech"
    }
}