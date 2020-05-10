package ru.digipeople.photo.helper

import android.net.Uri

/**
 * Содержит информацию о файле
 *
 * @author Kashonkov Nikita
 */
data class FileInfo(
        /**
         * Uri
         */
        val uri: Uri,
        /**
         * Name
         */
        val name: String)