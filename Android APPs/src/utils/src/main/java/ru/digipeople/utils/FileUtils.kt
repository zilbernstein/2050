package ru.digipeople.utils

import ru.digipeople.logger.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Вспомогательный класс для конвертаций файл в массив байтов
 *
 * @author Kashonkov Nikita
 */
class FileUtils {
    companion object {
        /**
         * добавление логгирования
         */
        private val logger = LoggerFactory.getLogger(FileUtils::class)

        fun covertToByteArray(file: File): ByteArray {
            val fis = FileInputStream(file)
            val bArray = ByteArray(file.length().toInt())
            try {
                fis.read(bArray)
            } catch (ioExp: IOException) {
                logger.error(ioExp)
            } finally {
                fis.close()
            }

            return bArray

        }
    }
}