package ru.digipeople.thingworx.filetransfer

import android.os.Environment
import com.thingworx.communications.client.AndroidConnectedThingClient
import com.thingworx.communications.client.things.filetransfer.FileTransferVirtualThing
import java.io.File

/**
 * Передача файлов с помощью платформы ThingWorx
 *
 * @author Kashonkov Nikita
 */
class FileTransferThing(client: AndroidConnectedThingClient, thingName: String) : FileTransferVirtualThing(thingName, "transfer", client) {
    init {
        /**
         * формирование строки
         */
        val path = StringBuilder()
                .append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).canonicalPath)
                .append(File.separator)
                .append(LOCOTECH_DIRECTORY)
                .toString()

        addVirtualDirectory(OUT_FIELD, path)
        addVirtualDirectory(IN_FIELD, path)
    }

    companion object {
        const val DEMO_TRANSFER = "IMAGETEST"
        const val LOCOTECH_DIRECTORY = "Locotech"
        const val OUT_FIELD = "out"
        const val IN_FIELD = "in"
    }
}